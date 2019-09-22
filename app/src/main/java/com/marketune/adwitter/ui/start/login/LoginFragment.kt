package com.marketune.adwitter.ui.start.login

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
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.LoginFragmentBinding
import com.marketune.adwitter.models.TokenManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty

/**
 * @author: Abdel-Rahman El-Shikh :)
 */

private const val TAG = "LoginFragment"

class LoginFragment : Fragment(), TextWatcher {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var mViewModel: LoginViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        binding.edtEmail.addTextChangedListener(this)
        binding.edtPassword.addTextChangedListener(this)
        binding.goToRegister.setOnClickListener { controller().navigate(R.id.action_login_fragment_to_registerFragment) }
        binding.btnLogin.setOnClickListener { login() }
        tokenManager = TokenManager.getInstance(context)
        return binding.root
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
            getFcmToken()
        }
    }

    private fun getFcmToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                //  Toast.makeText(activity,task.result!!.token,Toast.LENGTH_SHORT).show()
                loginFromBackEnd(task.result!!.token)
            })

    }

    private fun loginFromBackEnd(token: String) {
        Log.i(TAG, "FCM_TOKEN SUCCESS - > : $token")
        mViewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        mViewModel.init()
        mViewModel.login(
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString(),
            token
        ).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    tokenManager.saveToken(it.data)
                    controller().navigate(R.id.actionToMain)
                }
                Status.ERROR -> Toast.makeText(
                    activity,
                    it.apiError!!.message,
                    Toast.LENGTH_SHORT
                ).show()
                Status.FAILURE -> Log.e(
                    TAG,
                    "loginFromBackEnd : ${it.apiException!!.localizedMessage}"
                )
            }
            binding.progressBar.visibility = View.GONE
        })

    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.tilEmail.error = null
        binding.tilPassword.error = null
    }

    private fun controller(): NavController {
        return NavHostFragment.findNavController(this)
    }
}