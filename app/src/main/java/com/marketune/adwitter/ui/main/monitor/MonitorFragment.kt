package com.marketune.adwitter.ui.main.monitor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.marketune.adwitter.R
import com.marketune.adwitter.databinding.MonitorFragmentBinding

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/25/2019
 */
class MonitorFragment : Fragment(){
    private lateinit var binding: MonitorFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.monitor_fragment,container,false)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.monitor)

        return binding.root
    }
}