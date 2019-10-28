package com.marketune.adwitter.ui.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.BankFragmentBinding

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/28/2019
 */
class BankFragment : Fragment() {
    private lateinit var binding: BankFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bank_fragment,container,false)
        return binding.root
    }
}