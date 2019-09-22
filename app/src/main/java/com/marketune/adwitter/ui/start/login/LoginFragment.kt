package com.marketune.adwitter.ui.start.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.LoginFragmentBinding
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty

/**
 * @author: Abdel-Rahman El-Shikh :)
 */
class LoginFragment : Fragment() , TextWatcher{


    private lateinit var binding: LoginFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment,container,false)
        binding.edtEmail.addTextChangedListener(this)
        binding.edtPassword.addTextChangedListener(this)
        binding.goToRegister.setOnClickListener {controller().navigate(R.id.action_login_fragment_to_registerFragment)}
        binding.btnLogin.setOnClickListener{login()}
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
        if(isValidData){

        }
    }
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        binding.tilEmail.error = null
        binding.tilPassword.error = null
    }

    private fun controller():NavController{
        return NavHostFragment.findNavController(this)
    }
}