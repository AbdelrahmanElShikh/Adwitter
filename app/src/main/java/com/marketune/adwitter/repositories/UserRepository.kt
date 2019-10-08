package com.marketune.adwitter.repositories

import androidx.lifecycle.MutableLiveData
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.api.RequestHandler
import com.marketune.adwitter.api.RetrofitBuilder
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterAccount
import com.marketune.adwitter.models.User
import retrofit2.Call

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/23/2019
 */
class UserRepository {
    companion object {
        private var userRepository: UserRepository? = null

        fun getInstance(): UserRepository {
            if (userRepository == null)
                userRepository = UserRepository()
            return userRepository!!
        }
    }

    fun getUserInfo(tokenManager: TokenManager): MutableLiveData<ApiResponse<User>> {
        val apiService = RetrofitBuilder.createServiceWithAuth(tokenManager)
        val requestHandler = object : RequestHandler<User>() {
            override fun makeRequest(): Call<User> {
                return apiService.getUserInfo()
            }
        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }

    fun getUserTwitterAccounts(tokenManager: TokenManager): MutableLiveData<ApiResponse<List<TwitterAccount>>> {
        val apiService = RetrofitBuilder.createServiceWithAuth(tokenManager)
        val requestHandler = object : RequestHandler<List<TwitterAccount>>() {
            override fun makeRequest(): Call<List<TwitterAccount>> {
                return apiService.getTwitterAccounts()
            }
        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }

    fun addTwitterAccount(
        tokenManager: TokenManager,
        name: String,
        imageUri: String,
        followers: Int,
        providerId: String,
        oauthToken: String,
        oauthSecret: String
    ): MutableLiveData<ApiResponse<List<TwitterAccount>>> {
        val apiService = RetrofitBuilder.createServiceWithAuth(tokenManager)
        val requestHandler = object : RequestHandler<List<TwitterAccount>>() {
            override fun makeRequest(): Call<List<TwitterAccount>> {
                return apiService.addAccount(
                    name = name,
                    imageUri = imageUri,
                    followers = followers,
                    providerId = providerId,
                    oauthToken = oauthToken,
                    oauthSecret = oauthSecret
                )
            }
        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()

    }
}