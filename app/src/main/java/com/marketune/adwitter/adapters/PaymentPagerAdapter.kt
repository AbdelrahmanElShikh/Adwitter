package com.marketune.adwitter.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.marketune.adwitter.R
import com.marketune.adwitter.ui.main.payment.BankFragment
import com.marketune.adwitter.ui.main.payment.PaypalFragment

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/27/2019
 */
class PaymentPagerAdapter(private val context: Context?, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                BankFragment()
            }
            else -> {
                PaypalFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
          0->  context!!.getString(R.string.bank_account)
          else -> context!!.getString(R.string.paypal)
        }
    }
}