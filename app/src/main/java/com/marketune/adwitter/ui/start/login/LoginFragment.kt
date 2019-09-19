package com.marketune.adwitter.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.LoginFragmentBinding

/**
 * Created By Abdel-Rahman El-Shikh
 */
class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment,container,false)
        binding.goToRegister.setOnClickListener {controller().navigate(R.id.action_login_fragment_to_registerFragment)}
        return binding.root
    }
    private fun controller():NavController{
        return NavHostFragment.findNavController(this)
    }
}