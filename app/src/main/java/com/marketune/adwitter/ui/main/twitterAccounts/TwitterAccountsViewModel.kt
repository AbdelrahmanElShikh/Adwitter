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
    private lateinit var response: MutableLiveData<ApiResponse<List<TwitterAccount>>>
    fun init() {
        instance = UserRepository.getInstance()
    }

    fun getUserTwitterAccounts(tokenManager: TokenManager): LiveData<ApiResponse<List<TwitterAccount>>> {
        response = instance.getUserTwitterAccounts(tokenManager)
        return response
    }

    fun reconnectAccount(
        tokenManager: TokenManager,
        accountId: Int,
        name: String,
        imageUri: String,
        followers: Int,
        oauthToken: String,
        oauthSecret: String
    ): LiveData<ApiResponse<List<TwitterAccount>>> {
        return instance.reconnectAccount(
            tokenManager = tokenManager,
            accountId = accountId,
            name = name,
            imageUri = imageUri,
            followers = followers,
            oauthToken = oauthToken,
            oauthSecret = oauthSecret
        )
    }
}