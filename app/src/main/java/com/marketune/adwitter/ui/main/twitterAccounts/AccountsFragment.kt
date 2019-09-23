package com.marketune.adwitter.ui.main.twitterAccounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.AccountsFragmentBinding

/**
 * Created By Abdel-Rahman El-Shikh
 */
class AccountsFragment : Fragment() {
    private lateinit var binding: AccountsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.accounts_fragment,container,false)
        return binding.root
    }
}