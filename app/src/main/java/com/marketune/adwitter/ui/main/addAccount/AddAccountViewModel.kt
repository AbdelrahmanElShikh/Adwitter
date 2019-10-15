package com.marketune.adwitter.ui.main.addAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.models.Target
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterAccount
import com.marketune.adwitter.repositories.UserRepository

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/14/2019
 */
class AddAccountViewModel : ViewModel(){
    private lateinit var instance: UserRepository
    fun init() {
        instance = UserRepository.getInstance()
    }
    fun getTargetData(tokenManager: TokenManager):LiveData<ApiResponse<Target>>{
        return instance.getTargetData(tokenManager)
    }
    fun addTwitterAccount(
        tokenManager: TokenManager,
        name: String,
        imageUri: String,
        followers: Int,
        providerId: String,
        oauthToken: String,
        oauthSecret: String,
        targetIds : List<Int>
    ): LiveData<ApiResponse<List<TwitterAccount>>> {
        return instance.addTwitterAccount(
            tokenManager = tokenManager,
            name = name,
            imageUri = imageUri,
            followers = followers,
            providerId = providerId,
            oauthToken = oauthToken,
            oauthSecret = oauthSecret,
            targetIds = targetIds
        )
    }
}