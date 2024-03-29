package com.marketune.adwitter.ui.main.twitterAccounts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.marketune.adwitter.R
import com.marketune.adwitter.adapters.AccountsAdapter
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.AccountsFragmentBinding
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterAccount
import com.marketune.adwitter.models.TwitterUser
import com.marketune.adwitter.ui.main.MainActivity


/**
 * Created By Abdel-Rahman El-Shikh
 */
private const val TAG = "AccountsFragment"

class AccountsFragment : Fragment(), AccountsAdapter.OnAccountSelected,
    MainActivity.OnAccountReceived {
    private lateinit var binding: AccountsFragmentBinding
    private lateinit var mViewModel: TwitterAccountsViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var mAdapter: AccountsAdapter
    private var accountId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.accounts_fragment, container, false)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.home)
        mAdapter = AccountsAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
        mViewModel = ViewModelProviders.of(this)[TwitterAccountsViewModel::class.java]
        tokenManager = TokenManager.getInstance(activity)
        binding.fab.setOnClickListener { controller().navigate(R.id.addAccountFragment) }
        mViewModel.init()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.initUserTwitterAccounts(tokenManager)
        setUpAccountsObservable()
    }

    private fun setUpAccountsObservable() {
        binding.progressBar.visibility = View.VISIBLE
        mViewModel.getUserTwitterAccounts().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.isNotEmpty()) {
                        binding.txtNoAccounts.visibility = View.GONE
                        mAdapter.setAccounts(it.data!!)
                    } else {
                        mAdapter.setAccounts(null)
                        binding.txtNoAccounts.visibility = View.VISIBLE
                    }

                }
                Status.ERROR -> {
                    Toast.makeText(activity, it.apiError?.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onActivityCreated -> Error  ${it.apiError?.message}")
                }
                Status.FAILURE -> {
                    showFailureSnackbar()
                    Log.e(TAG, "onActivityCreated -> Failure  ${it.apiException?.localizedMessage}")
                }
            }
            binding.progressBar.visibility = View.GONE
        })
    }

    override fun onAccountSelected(account: TwitterAccount) {
        try {
            val twitterUserId = (account.provider_id)
            Log.e(TAG, "$twitterUserId")
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?user_id=${twitterUserId}")
                )
            )
        } catch (e: Exception) {
            val twitterUserName = account.name
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/${twitterUserName}")
                )
            )
        }
    }

    override fun onAccountReconnectClicked(accountId: Int) {
        this.accountId = accountId
        (activity as MainActivity).authorize(this)
    }

    override fun onAccountReceived(user: TwitterUser) {
        mViewModel.initGetReconnectionResponse(
            tokenManager = tokenManager,
            accountId = accountId,
            name = user.name,
            imageUri = user.profileImageUrl,
            followers = user.followersCount,
            oauthToken = user.token,
            oauthSecret = user.secret
        )
        setReconnectAccountObservable()
    }

    private fun setReconnectAccountObservable() {
        binding.progressBar.visibility = View.VISIBLE
        mViewModel.getReconnectAccountResponse().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.isNotEmpty()) {
                        binding.txtNoAccounts.visibility = View.GONE
                        mAdapter.setAccounts(it.data!!)
                    } else {
                        mAdapter.setAccounts(null)
                        binding.txtNoAccounts.visibility = View.VISIBLE
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(activity, it.apiError?.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onActivityCreated -> Error  ${it.apiError?.message}")
                }
                Status.FAILURE -> {
                    showFailureSnackbar()
                    Log.e(TAG, "onActivityCreated -> Failure  ${it.apiException?.localizedMessage}")
                }
            }
            binding.progressBar.visibility = View.GONE

        })
    }

    private fun showFailureSnackbar() {
        Snackbar.make(
            binding.rootLayout,
            getString(R.string.internet_issue),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            getString(R.string.retry)
        ) {
            binding.progressBar.visibility = View.VISIBLE
            mViewModel.initUserTwitterAccounts(tokenManager)
        }
            .show()
    }

    private fun controller(): NavController {
        return NavHostFragment.findNavController(this)
    }
}