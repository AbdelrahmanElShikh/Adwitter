package com.marketune.adwitter.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.navigation.NavigationView
import com.marketune.adwitter.R
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.ActivtyMainBinding
import com.marketune.adwitter.databinding.NavHeaderBinding
import com.marketune.adwitter.helpers.TwitterAuthResponse
import com.marketune.adwitter.helpers.TwitterHelper
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterUser
import com.marketune.adwitter.ui.start.login.LoginActivity
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.TwitterCore


/**
 * @author: Abdel-Rahman El-Shikh :)
 */
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener ,
    TwitterAuthResponse {
    private lateinit var binding: ActivtyMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navHeaderBinding: NavHeaderBinding
    private lateinit var mViewModel: UserViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var twitterHelper: TwitterHelper
    private lateinit var listener: OnAccountReceived


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        twitterHelper = TwitterHelper(
            this,
            this,
            getString(R.string.twitterApaiKey),
            getString(R.string.twitterSecretKey)
        )
        binding = DataBindingUtil.setContentView(this, R.layout.activty_main)
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_humbrger)

        val navigationView = binding.navView
        navHeaderBinding = NavHeaderBinding.bind(navigationView.getHeaderView(0))
        tokenManager = TokenManager.getInstance(this)
        getUser()
        navController = Navigation.findNavController(this, R.id.main_nav_host_fragment)
        navigationView.itemIconTintList = null
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener(this)
        //google variable initialization
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()

    }

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onStop() {
        super.onStop()
        googleApiClient.disconnect()
    }

    fun addAccount(listener: OnAccountReceived){
        this.listener = listener
        twitterHelper.performSignIn()

    }


    private fun getUser() {
        mViewModel = ViewModelProviders.of(this)[UserViewModel::class.java]
        mViewModel.init()
        mViewModel.getUserInfo(tokenManager).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    navHeaderBinding.user = it.data
                    Picasso.get().load(it.data!!.avatar)
                        .placeholder(getDrawable(R.drawable.ic_owner)!!)
                        .into(navHeaderBinding.imageProfile)
                }
                Status.ERROR -> {
                    Log.e(TAG, "getUser Error: ${it.apiError?.message}")
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                else -> Log.e(TAG, "getUser Failure : ${it.apiException?.localizedMessage}")
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(
                this,
                R.id.main_nav_host_fragment
            ), drawerLayout
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_home -> navController.navigate(R.id.accountsFragment)
            R.id.nav_monitor -> navController.navigate(R.id.monitorFragment)
            R.id.nav_edt_bank_info -> navController.navigate(R.id.editBankInfoFragment)
            R.id.nav_payment -> navController.navigate(R.id.paymentFragment)
            R.id.nav_support -> openWhatsApp(this)
            R.id.nav_logout -> {
                socialLogOut()
            }
            else -> Toast.makeText(this, "else", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private fun openWhatsApp(context: Context) {


    }
    override fun onTwitterError() {
        Toast.makeText(this, "Twitter sign in failed.", Toast.LENGTH_SHORT).show()

    }

    override fun onTwitterProfileReceived(user: TwitterUser) {
        listener.onAccountReceived(user)
    }

    private fun socialLogOut() {
        when {
            isGoogleUserSignIn() -> {
                Log.e("MainActivity", "Signing out from Google")
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback { status ->
                    if (status.isSuccess)
                        deleteTokenAndNavigate()
                }
            }
            isTwitterSessionActive() -> {
                Log.w("MainActivity", "Signing out from Twitter")
                TwitterCore.getInstance().sessionManager.clearActiveSession()
                deleteTokenAndNavigate()
            }
            isFacebookUserActive() -> {
                Log.w("MainActivity", "Signing out from Facebook")
                LoginManager.getInstance().logOut()
                deleteTokenAndNavigate()
            }
            else -> deleteTokenAndNavigate()
        }


    }

    private fun deleteTokenAndNavigate() {
        tokenManager.deleteToken()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    private fun isGoogleUserSignIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null
    }

    private fun isTwitterSessionActive(): Boolean {
        val twitterSession = TwitterCore.getInstance().sessionManager.activeSession
        return twitterSession != null
    }

    private fun isFacebookUserActive(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null
    }
    interface OnAccountReceived{
        fun onAccountReceived(user:TwitterUser)
    }
}