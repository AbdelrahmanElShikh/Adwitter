package com.marketune.adwitter.ui.main.twitterAccounts

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.marketune.adwitter.R
import com.marketune.adwitter.adapters.AccountsAdapter
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.AccountsFragmentBinding
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterAccount


/**
 * Created By Abdel-Rahman El-Shikh
 */
private const val TAG = "AccountsFragment"
class AccountsFragment : Fragment(),AccountsAdapter.OnAccountSelected{
    private lateinit var binding: AccountsFragmentBinding
    private lateinit var mViewModel: TwitterAccountsViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var mAdapter : AccountsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.accounts_fragment,container,false)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.home)
        mAdapter =AccountsAdapter(this)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
        mViewModel = ViewModelProviders.of(this)[TwitterAccountsViewModel::class.java]
        tokenManager = TokenManager.getInstance(activity)
        mViewModel.init()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.getUserTwitterAccounts(tokenManager).observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS->{
                    mAdapter.setAccounts(it.data!!)
                }
                Status.ERROR -> {
                    Toast.makeText(activity,it.apiError?.message,Toast.LENGTH_SHORT).show()
                    Log.e(TAG,"onActivityCreated -> Error  ${it.apiError?.message}")
                }
                Status.FAILURE->{
                    Toast.makeText(activity,"Failed To Connect",Toast.LENGTH_SHORT).show()
                    Log.e(TAG,"onActivityCreated -> Failure  ${it.apiException?.localizedMessage}")
                }
            }
            binding.progressBar.visibility = View.GONE
        })
    }
    override fun onAccountSelected(account: TwitterAccount) {
        Toast.makeText(activity,"hello",Toast.LENGTH_SHORT).show()
    }
}