package com.marketune.adwitter.ui.main.editBankInfo

import android.os.Bundle
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
import com.marketune.adwitter.R
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.EditBankInfoFragmentBinding
import com.marketune.adwitter.models.Details
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.ui.main.UserViewModel

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/25/2019
 */
class EditBankInfoFragment : Fragment() {
    private lateinit var binding: EditBankInfoFragmentBinding
    private lateinit var mViewModel : UserViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var userDetails : Details

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_bank_info_fragment,container,false)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.bank_information)
        mViewModel = ViewModelProviders.of(this)[UserViewModel::class.java]
        tokenManager = TokenManager.getInstance(activity)
        mViewModel.init()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
        getUserBankDetails()
    }

    private fun getUserBankDetails() {
        mViewModel.getUserInfo(tokenManager = tokenManager).observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    userDetails = it.data!!.details
                    binding.details = userDetails
                }
                Status.ERROR -> {
                    Toast.makeText(activity,getString(R.string.error_bank_details),Toast.LENGTH_SHORT).show()
                    controller().navigate(R.id.accountsFragment)
                }
                Status.FAILURE -> {
                    Toast.makeText(activity,getString(R.string.internet_issue),Toast.LENGTH_SHORT).show()
                    controller().navigate(R.id.accountsFragment)
                }
            }
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun controller(): NavController {
        return NavHostFragment.findNavController(this)
    }
}