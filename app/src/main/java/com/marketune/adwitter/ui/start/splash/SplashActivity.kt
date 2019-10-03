package com.marketune.adwitter.ui.start.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.SplashActivityBinding
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.ui.main.MainActivity
import com.marketune.adwitter.ui.start.login.LoginActivity

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
        if (tokenManager.getToken().access_token != null) {
            goToMainActivity()
        } else
            goToLoginActivity()
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