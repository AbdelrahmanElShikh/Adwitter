package com.marketune.adwitter.ui.main.addAccount

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.marketune.adwitter.R
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.databinding.AddAccountFragmentBinding
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterUser
import com.marketune.adwitter.ui.main.MainActivity

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/14/2019
 */
private const val TAG = "AddAccountFragment"

class AddAccountFragment : Fragment() ,MainActivity.OnAccountReceived{
    private lateinit var binding: AddAccountFragmentBinding
    private lateinit var mViewModel: AddAccountViewModel
    private lateinit var tokenManager: TokenManager
    private var interestMap = mutableMapOf<Int, String>()
    private var areaMap = mutableMapOf<Int, String>()
    private var ageMap = mutableMapOf<Int, String>()
    private var interestCheckedItems = arrayListOf<Boolean>()
    private var areaCheckedItems = arrayListOf<Boolean>()
    private var ageCheckedItems = arrayListOf<Boolean>()
    private var selectedItems = arrayListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_account_fragment, container, false)
        (activity as AppCompatActivity).supportActionBar!!.title =
            getString(R.string.targeting_setting)
        tokenManager = TokenManager.getInstance(activity)
        binding.btnAddTwitterAccount.setOnClickListener { addTwitterAccount() }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getTargetData()
        binding.btnInterest.setOnClickListener {
            showTargetOptions(
                getString(R.string.choose_interest),
                interestMap,
                interestCheckedItems
            )
        }
        binding.btnArea.setOnClickListener {
            showTargetOptions(
                getString(R.string.choose_area),
                areaMap,
                areaCheckedItems
            )
        }
        binding.btnAge.setOnClickListener {
            showTargetOptions(
                getString(R.string.choose_age),
                ageMap,
                ageCheckedItems
            )
        }
    }


    private fun getTargetData() {
        binding.progressBar.visibility = View.VISIBLE
        mViewModel = ViewModelProviders.of(this)[AddAccountViewModel::class.java]
        mViewModel.init()
        mViewModel.getTargetData(tokenManager).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    interestCheckedItems.clear()
                    for (interest in it.data!!.interest) {
                        interestMap[interest.id] = interest.langs.en
                        interestCheckedItems.add(false)
                    }
                    areaCheckedItems.clear()
                    for (area in it.data!!.area) {
                        areaMap[area.id] = area.langs.en
                        areaCheckedItems.add(false)
                    }
                    ageCheckedItems.clear()
                    for (age in it.data!!.age) {
                        ageMap[age.id] = age.langs.en
                        ageCheckedItems.add(false)
                    }
                }
                Status.ERROR -> Toast.makeText(
                    activity,
                    it.apiError!!.message,
                    Toast.LENGTH_SHORT
                ).show()
                Status.FAILURE -> {
                    Log.e(
                        TAG,
                        "getTargetData : ${it.apiException!!.localizedMessage}"
                    )
                    Toast.makeText(activity, it.apiException!!.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun showTargetOptions(
        title: String,
        targetMap: MutableMap<Int, String>,
        targetCheckedItems: ArrayList<Boolean>
    ) {
        val builder = AlertDialog.Builder(activity!!)
        val interestsArrayValues = targetMap.values.toTypedArray()
        val interestsArrayKeys = targetMap.keys.toTypedArray()
        builder.setTitle(title)
        builder.setMultiChoiceItems(
            interestsArrayValues,
            targetCheckedItems.toBooleanArray()
        ) { _, which, isChecked ->
            targetCheckedItems[which] = isChecked
            if (isChecked)
                selectedItems.add(interestsArrayKeys[which])
            else
                selectedItems.remove(interestsArrayKeys[which])
        }
        val dialog = builder.create()
        dialog.show()
    }
    private fun addTwitterAccount(){

        if(interestCheckedItems.contains(true) && areaCheckedItems.contains(true) && ageCheckedItems.contains(true))
        {
            (activity as MainActivity).authorize(this)
        }else
            Toast.makeText(activity,getString(R.string.target_missing),Toast.LENGTH_SHORT).show()
    }

    override fun onAccountReceived(user: TwitterUser) {
        binding.progressBar.visibility = View.VISIBLE
        mViewModel.addTwitterAccount(
            tokenManager = tokenManager,
            name = user.name,
            imageUri = user.profileImageUrl,
            followers = user.followersCount,
            providerId = user.id,
            oauthToken = user.token,
            oauthSecret = user.secret,
            targetIds = selectedItems
        ).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(
                        activity,
                        getString(R.string.add_account_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    controller().navigate(R.id.accountsFragment)
                }
                Status.ERROR -> {
                    if (it.apiError!!.code == 402) {
                        Toast.makeText(activity, it!!.apiError!!.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, it!!.apiError!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
                Status.FAILURE -> {
                    Toast.makeText(activity, "Failed To Connect 404", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onAccountReceived - > Failure ${it.apiException?.localizedMessage}")
                }
            }
            binding.progressBar.visibility = View.GONE

        })
    }
    private fun controller(): NavController {
        return NavHostFragment.findNavController(this)
    }
}