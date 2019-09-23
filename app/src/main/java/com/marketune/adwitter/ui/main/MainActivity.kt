package com.marketune.adwitter.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.marketune.adwitter.R
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.ActivtyMainBinding
import com.marketune.adwitter.databinding.NavHeaderBinding
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.ui.start.LoginActivity
import com.squareup.picasso.Picasso


/**
 * @author: Abdel-Rahman El-Shikh :)
 */
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivtyMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var navHeaderBinding: NavHeaderBinding
    private lateinit var mViewModel: UserViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activty_main)
        drawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        toggle.syncState()

        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = false
        val drawable = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_humbrger,
            this.theme
        )
        toggle.setHomeAsUpIndicator(drawable)
        toggle.setToolbarNavigationClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }


        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setHomeButtonEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
////        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_humbrger)
//        toggle.isDrawerIndicatorEnabled = false
//        binding.toolbar.setNavigationIcon(R.drawable.ic_humbrger)
        val navigationView = binding.navView
        navHeaderBinding = NavHeaderBinding.bind(navigationView.getHeaderView(0))
        tokenManager = TokenManager.getInstance(this)
        getUser()
        navController = Navigation.findNavController(this, R.id.main_nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(navigationView, navController)
        navigationView.setNavigationItemSelectedListener(this)
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
                Status.ERROR -> Log.e(TAG, "getUser: ${it.apiError?.message}")
                else -> Log.e(TAG, "getUser: ${it.apiException?.localizedMessage}")
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
            R.id.nav_monitor -> Toast.makeText(this, "Monitor", Toast.LENGTH_SHORT).show()
            R.id.nav_logout -> {
                tokenManager.deleteToken()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            else -> Toast.makeText(this, "else", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}