package com.marketune.adwitter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marketune.adwitter.databinding.AccountItemBinding
import com.marketune.adwitter.models.TwitterAccount
import com.squareup.picasso.Picasso

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/7/2019
 */
class AccountsAdapter constructor(var listener: OnAccountSelected) :
    RecyclerView.Adapter<AccountsAdapter.ViewHolder>() {
    private var accountList: List<TwitterAccount>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AccountItemBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val account = this.accountList!![position]
        holder.binding.account = account
        Picasso.get().load(account.avatar).into(holder.binding.accountImage)
    }

    override fun getItemCount(): Int {
        return if(accountList == null) 0
        else accountList!!.size
    }

    fun setAccounts(accountList: List<TwitterAccount>?) {
        this.accountList = accountList
        notifyDataSetChanged()
    }

    interface OnAccountSelected {
        fun onAccountSelected(account: TwitterAccount)
        fun onAccountReconnectClicked(accountId : Int)
    }

    inner class ViewHolder(var binding: AccountItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onClick() }
            binding.btnReconnect.setOnClickListener { reconnectAccount() }

        }

        private fun reconnectAccount() {
            val accountId = accountList!![adapterPosition].id
            listener.onAccountReconnectClicked(accountId)
        }

        private fun onClick() {
            val account = accountList!![adapterPosition]
            listener.onAccountSelected(account)
        }
    }
}
