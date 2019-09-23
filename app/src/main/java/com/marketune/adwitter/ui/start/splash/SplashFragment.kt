package com.marketune.adwitter.ui.start.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.SplashFragmentBinding
import com.marketune.adwitter.models.TokenManager

/**
 * Created By Abdel-Rahman El-Shikh
 */
class SplashFragment : Fragment() {
    private lateinit var binding :SplashFragmentBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.splash_fragment,container,false)
        tokenManager = TokenManager.getInstance(activity)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed( { updateUi()},2000)
    }

    private fun updateUi() {
        if (tokenManager.getToken().access_token!=null){
            controller().navigate(R.id.actionToMain)
            activity!!.finish()
        }
        else
            controller().navigate(R.id.action_splash_fragment_to_login_fragment)
    }
    private fun controller():NavController{
        return NavHostFragment.findNavController(this)
    }
}