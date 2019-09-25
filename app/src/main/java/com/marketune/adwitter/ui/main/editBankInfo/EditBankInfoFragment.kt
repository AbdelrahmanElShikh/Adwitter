package com.marketune.adwitter.ui.main.editBankInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.EditBankInfoFragmentBinding

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/25/2019
 */
class EditBankInfoFragment : Fragment() {
    private lateinit var binding: EditBankInfoFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.edit_bank_info_fragment,container,false)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.bank_information)

        return binding.root
    }
}