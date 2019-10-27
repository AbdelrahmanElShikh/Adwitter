package com.marketune.adwitter.ui.start.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.SplashActivityBinding
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.ui.main.MainActivity
import com.marketune.adwitter.ui.start.login.LoginActivity
import com.marketune.adwitter.utils.Tools

/**
 * Created By Abdel-Rahman El-Shikh
 */
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: SplashActivityBinding
    private lateinit var tokenManager: TokenManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity)
        tokenManager = TokenManager.getInstance(this)
    }


    override fun onStart() {
        super.onStart()
        Handler().postDelayed({ updateUi() }, 2000)
    }

    private fun updateUi() {
        binding.progressBar.visibility = View.VISIBLE
        if(Tools.checkInternetConnection(this)){
            if (tokenManager.getToken().access_token != null) {
                goToMainActivity()
            } else
                goToLoginActivity()
        }else{
            binding.progressBar.visibility = View.GONE
            showFailureSnackbar()
        }

    }
    private fun showFailureSnackbar() {
        Snackbar.make(
            binding.rootLayout,
            getString(R.string.internet_issue),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            getString(R.string.retry)
        ) {
            updateUi()
        }
            .show()
    }

    private fun goToLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }

}