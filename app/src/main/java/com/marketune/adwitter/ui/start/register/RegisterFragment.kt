package com.marketune.adwitter.ui.start.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.iid.FirebaseInstanceId
import com.marketune.adwitter.R
import com.marketune.adwitter.api.ApiError
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.RegisterFragmentBinding
import com.marketune.adwitter.models.AccessToken
import com.marketune.adwitter.models.TokenManager
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail

/**
 * @author : Abdel-Rahman El-Shikh
 */
private const val TAG = "RegisterFragment"

class RegisterFragment : Fragment(), TextWatcher {

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var mViewModel: RegisterViewModel
    private lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.register_fragment, container, false)

        binding.edtPassword.addTextChangedListener(this)
        binding.edtConfirmPassword.addTextChangedListener(this)
        binding.btnRegister.setOnClickListener { register() }
        binding.btnBack.setOnClickListener { controller().navigate(R.id.action_registerFragment_to_login_fragment) }
        tokenManager = TokenManager.getInstance(context)
        return binding.root
    }

    private fun register() {
        var isValidData = true
        if (!(checkEmpty(binding.edtUserName) || checkEmpty(binding.edtEmail))) {
            binding.edtPassword.nonEmpty {
                binding.tilPassword.error = getString(R.string.enter_password)
                isValidData = false
            }
            if (binding.edtPassword.text.toString() != binding.edtConfirmPassword.text.toString()) {
                binding.tilPasswordConfirm.error = getString(R.string.not_identical)
                isValidData = false
            } else {
                binding.edtEmail.validEmail {
                    binding.edtEmail.error = it
                    isValidData = false
                }
            }
        } else {
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
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                //  Toast.makeText(activity,task.result!!.token,Toast.LENGTH_SHORT).show()
                registerInBackEnd(task.result!!.token)
            })

    }

    private fun registerInBackEnd(token: String) {
        Log.i(TAG, "FCMTOKEN SUCCESS - > : $token")

        mViewModel = ViewModelProviders.of(this)[RegisterViewModel::class.java]
        mViewModel.init()
        mViewModel.registerNewUser(
            binding.edtUserName.text.toString(),
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString(),
            token
        )
            .observe(viewLifecycleOwner, Observer<ApiResponse<AccessToken>> {
                when (it.status) {
                    Status.SUCCESS -> {
                        tokenManager.saveToken(it.data)
                        controller().navigate(R.id.actionToMain)
                    }
                    Status.ERROR -> handleErrors(it.apiError)
                    else -> Log.e(TAG, "register FAILURE - > : " + it.apiException!!.localizedMessage)



                }
                binding.progressBar.visibility = View.GONE
            })
    }

    private fun checkEmpty(edtText: TextInputEditText): Boolean {
        var isEmpty = false
        edtText.nonEmpty {
            edtText.error = it
            isEmpty = true
        }
        return isEmpty
    }

//    private fun handleErrors(apiError: ApiError?) {
//        for (error in apiError!!.errors.entries) {
//            if (error.key == "name") {
//                binding.tilName.setError(error.value.get(0))
//            }
//            if (error.key == "email") {
//                binding.tilEmail.setError(error.value.get(0))
//            }
//            if (error.key == "password") {
//                binding.tilPassword.setError(error.value.get(0))
//            }
//        }
//    }
    private fun handleErrors(apiError: ApiError?){
        apiError!!.errors.entries.forEach{(key,value) ->
            when(key){
                "name"-> binding.tilName.error = value[0]
                "email"->binding.tilEmail.error = value[0]
                else -> binding.tilPassword.error = value[0]

            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.tilPassword.error = null
        binding.tilPasswordConfirm.error = null
    }

    private fun controller(): NavController {
        return NavHostFragment.findNavController(this)
    }
}
/**
 * Notes : In the "Observe" method i used (viewLifecycleOwner) instead of (this) so,
 * that LiveData will remove observers every time the fragment’s view is destroyed
 */



