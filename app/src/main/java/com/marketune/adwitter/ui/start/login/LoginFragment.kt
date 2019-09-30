package com.marketune.adwitter.ui.start.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.marketune.adwitter.R
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.LoginFragmentBinding
import com.marketune.adwitter.helpers.GoogleHelper
import com.marketune.adwitter.helpers.TwitterAuthResponse
import com.marketune.adwitter.helpers.TwitterHelper
import com.marketune.adwitter.models.AccessToken
import com.marketune.adwitter.models.GoogleUser
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterUser
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty

/**
 * @author: Abdel-Rahman El-Shikh :)
 */

private const val TAG = "LoginFragment"
private const val REGULAR_LOGIN_FCM_TOKEN = 0
private const val GOOGLE_LOGIN_FCM_TOKEN = 1
private const val TWITTER_LOGIN_FCM_TOKEN = 2


class LoginFragment : Fragment(), TextWatcher, GoogleHelper.GoogleAuthResponse,
    TwitterAuthResponse {


    private lateinit var binding: LoginFragmentBinding
    private lateinit var mViewModel: LoginViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var googleHelper: GoogleHelper
    private lateinit var twitterHelper: TwitterHelper
    private lateinit var googleUser: GoogleUser
    private lateinit var twitterUser: TwitterUser
    private lateinit var observer: Observer<ApiResponse<AccessToken>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleHelper = GoogleHelper(activity!!, this, this)
        twitterHelper = TwitterHelper(activity!!,this,this,getString(R.string.twitterApaiKey),getString(R.string.twitterSecretKey))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        mViewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        mViewModel.init()
        binding.edtEmail.addTextChangedListener(this)
        binding.edtPassword.addTextChangedListener(this)
        binding.goToRegister.setOnClickListener { controller().navigate(R.id.action_login_fragment_to_registerFragment) }
        binding.btnLogin.setOnClickListener { login() }
        binding.googleLogin.setOnClickListener { googleHelper.performSignIn() }
        binding.twitterLogin.setOnClickListener{ twitterHelper.performSignIn() }
        tokenManager = TokenManager.getInstance(context)
        observer = Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    tokenManager.saveToken(it.data)
                    controller().navigate(R.id.actionToMain)
                    activity!!.finish()
                }
                Status.ERROR -> Toast.makeText(
                    activity,
                    it.apiError!!.message,
                    Toast.LENGTH_SHORT
                ).show()
                Status.FAILURE -> {
                    Log.e(
                        TAG,
                        "login : ${it.apiException!!.localizedMessage}"
                    )
                    Toast.makeText(activity, it.apiException!!.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progressBar.visibility = View.GONE
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        googleHelper.onActivityResult(requestCode, resultCode, data)
        twitterHelper.onActivityResult(requestCode,resultCode,data)
    }

    private fun login() {
        var isValidData = true
        binding.edtEmail.nonEmpty {
            binding.tilEmail.error = it
            isValidData = false
        }
        binding.edtPassword.nonEmpty {
            binding.tilPassword.error = it
            isValidData = false
        }
        if (isValidData) {
            binding.progressBar.visibility = View.VISIBLE
            getFcmToken(REGULAR_LOGIN_FCM_TOKEN)
        }
    }

    private fun getFcmToken(loginType: Int) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result!!.token
                when (loginType) {
                    0 -> loginFromBackEnd(token)
                    1 -> getGoogleToken(token)
                    2 -> getTwitterToken(token)
                    else -> {
                        //Do nth for now
                    }

                }
            })

    }

    private fun loginFromBackEnd(token: String) {
        Log.i(TAG, "FCM_TOKEN SUCCESS - > : $token")
        mViewModel.login(
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString(),
            token
        ).observe(viewLifecycleOwner, observer)

    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.tilEmail.error = null
        binding.tilPassword.error = null
    }

    /**
     *  handle Google SignIn success or failure.
     */
    override fun onGoogleAuthSignIn(user: GoogleUser) {
        this.googleUser = user
        getFcmToken(GOOGLE_LOGIN_FCM_TOKEN)
    }

    private fun getGoogleToken(token: String) {
        binding.progressBar.visibility = View.VISIBLE
        mViewModel.googleSocialLogin(
            name = googleUser.name,
            email = googleUser.email,
            provider = getString(R.string.google),
            providerId = googleUser.id,
            fcmToken = token,
            imageUri = googleUser.photoUri.toString()
        ).observe(viewLifecycleOwner, observer)

    }

    private fun getTwitterToken(token: String) {
        mViewModel.socialLogin(
            name = twitterUser.name,
            email = twitterUser.email,
            provider = getString(R.string.twitter),
            providerId = twitterUser.id,
            fcmToken = token,
            imageUri = twitterUser.profileImageUrl,
            oauthToken = twitterUser.token,
            oauthSecret = twitterUser.secret,
            followers = twitterUser.followersCount
            ).observe(viewLifecycleOwner, observer)
    }

    override fun onGoogleAuthSignInFailed() {
        Toast.makeText(activity, "Google sign in failed.", Toast.LENGTH_SHORT).show()
    }

    override fun onTwitterError() {
        Toast.makeText(activity, "Twitter sign in failed.", Toast.LENGTH_SHORT).show()
    }

    override fun onTwitterProfileReceived(user: TwitterUser) {
        this.twitterUser = user
        getFcmToken(TWITTER_LOGIN_FCM_TOKEN)
    }


    private fun controller(): NavController {
        return NavHostFragment.findNavController(this)
    }
}