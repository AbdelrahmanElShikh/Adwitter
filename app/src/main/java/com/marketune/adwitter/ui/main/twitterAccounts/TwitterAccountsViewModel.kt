package com.marketune.adwitter.ui.main.twitterAccounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterAccount
import com.marketune.adwitter.repositories.UserRepository

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/7/2019
 */
class TwitterAccountsViewModel : ViewModel() {
    private lateinit var instance: UserRepository
    private lateinit var userAccountObservable: MutableLiveData<ApiResponse<List<TwitterAccount>>>
    private lateinit var reconnectAccountObservable: MutableLiveData<ApiResponse<List<TwitterAccount>>>
    fun init() {
        instance = UserRepository.getInstance()
    }

    fun initUserTwitterAccounts(tokenManager: TokenManager) {
        userAccountObservable = instance.getUserTwitterAccounts(tokenManager)
    }
    fun getUserTwitterAccounts():LiveData<ApiResponse<List<TwitterAccount>>>{
        return userAccountObservable
    }

    fun initGetReconnectionResponse(
        tokenManager: TokenManager,
        accountId: Int,
        name: String,
        imageUri: String,
        followers: Int,
        oauthToken: String,
        oauthSecret: String
    ){
        reconnectAccountObservable = instance.reconnectAccount(
            tokenManager = tokenManager,
            accountId = accountId,
            name = name,
            imageUri = imageUri,
            followers = followers,
            oauthToken = oauthToken,
            oauthSecret = oauthSecret
        )
    }

    fun getReconnectAccountResponse(): LiveData<ApiResponse<List<TwitterAccount>>> {
        return reconnectAccountObservable
    }
}