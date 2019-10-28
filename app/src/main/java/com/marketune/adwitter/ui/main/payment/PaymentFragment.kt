package com.marketune.adwitter.ui.main.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.marketune.adwitter.R
import com.marketune.adwitter.adapters.PaymentPagerAdapter
import com.marketune.adwitter.databinding.PaymentTabsFragmentBinding

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/25/2019
 */
class PaymentFragment : Fragment() {
    private lateinit var binding: PaymentTabsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.payment_tabs_fragment,container,false)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.payment_and_financial)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.viewPager.adapter = PaymentPagerAdapter(context,childFragmentManager)
        return binding.root
    }
}