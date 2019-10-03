package com.marketune.adwitter.ui.start.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.iid.FirebaseInstanceId
import com.marketune.adwitter.R
import com.marketune.adwitter.api.ApiError
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.RegisterActivityBinding
import com.marketune.adwitter.models.AccessToken
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.ui.main.MainActivity
import com.marketune.adwitter.ui.start.login.LoginActivity
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail

/**
 * @author : Abdel-Rahman El-Shikh :)
 */
private const val TAG = "RegisterFragment"

class RegisterActivity : AppCompatActivity(), TextWatcher {

    private lateinit var binding: RegisterActivityBinding
    private lateinit var mViewModel: RegisterViewModel
    private lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.register_activity)
        binding.edtPassword.addTextChangedListener(this)
        binding.edtConfirmPassword.addTextChangedListener(this)
        binding.edtUserName.addTextChangedListener(this)
        binding.edtEmail.addTextChangedListener(this)
        binding.btnRegister.setOnClickListener { register() }
        binding.btnBack.setOnClickListener { goToLoginActivity() }
        tokenManager = TokenManager.getInstance(this)
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }


    /**
     * applying data validation on the user input and then,
     * if all data fields valid , performing backend registration with fcm token.
     */
    private fun register() {
        var isValidData = true
        if (!(checkEmpty(binding.edtUserName) || checkEmpty(binding.edtEmail))) {
            if (Character.isDigit(binding.edtUserName.text!![0])) {
                isValidData = false
                binding.tilName.error = getString(R.string.start_number_error)

            }
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
                registerInBackEnd(task.result!!.token)
            })

    }

    private fun registerInBackEnd(token: String) {
        Log.i(TAG, "FCM_TOKEN SUCCESS - > : $token")

        mViewModel = ViewModelProviders.of(this)[RegisterViewModel::class.java]
        mViewModel.init()
        mViewModel.registerNewUser(
            binding.edtUserName.text.toString(),
            binding.edtEmail.text.toString(),
            binding.edtPassword.text.toString(),
            token
        )
            .observe(this, Observer<ApiResponse<AccessToken>> {
                when (it.status) {
                    Status.SUCCESS -> {
                        tokenManager.saveToken(it.data)
                        goToMainActivity()
                    }
                    Status.ERROR -> handleErrors(it.apiError)
                    else -> Log.e(
                        TAG,
                        "register FAILURE - > : " + it.apiException!!.localizedMessage
                    )
                }
                binding.progressBar.visibility = View.GONE
            })
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

    private fun checkEmpty(edtText: TextInputEditText): Boolean {
        var isEmpty = false
        edtText.nonEmpty {
            edtText.error = it
            isEmpty = true
        }
        return isEmpty
    }

    private fun handleErrors(apiError: ApiError?) {
        apiError!!.errors.entries.forEach { (key, value) ->
            when (key) {
                "name" -> binding.tilName.error = value[0]
                "email" -> binding.tilEmail.error = value[0]
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
        binding.tilName.error = null
        binding.edtEmail.error = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToLoginActivity()
    }
}




