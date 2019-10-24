package com.marketune.adwitter.repositories

import androidx.lifecycle.MutableLiveData
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.api.RequestHandler
import com.marketune.adwitter.api.RetrofitBuilder
import com.marketune.adwitter.api.Status
import com.marketune.adwitter.models.Target
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.TwitterAccount
import com.marketune.adwitter.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/23/2019
 */
class UserRepository {
    private var twitterAccountsMutableLiveData =
        MutableLiveData<ApiResponse<List<TwitterAccount>>>()

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
        val apiResponse: ApiResponse<List<TwitterAccount>> = ApiResponse()
        apiService.getTwitterAccounts().enqueue(object : Callback<List<TwitterAccount>> {
            override fun onResponse(
                call: Call<List<TwitterAccount>>,
                response: Response<List<TwitterAccount>>
            ) {
                if (response.isSuccessful) {
                    apiResponse.data = response.body()
                    apiResponse.status = Status.SUCCESS
                    twitterAccountsMutableLiveData.value = apiResponse
                } else {
                    apiResponse.apiError =
                        RetrofitBuilder.convertErrors(response = response.errorBody())
                    apiResponse.status = Status.ERROR
                    twitterAccountsMutableLiveData.value = apiResponse
                }
            }


            override fun onFailure(call: Call<List<TwitterAccount>>, t: Throwable) {
                apiResponse.apiException = t as Exception
                apiResponse.status = Status.FAILURE
                twitterAccountsMutableLiveData.value = apiResponse
            }


        })
        return twitterAccountsMutableLiveData

    }

    fun addTwitterAccount(
        tokenManager: TokenManager,
        name: String,
        imageUri: String,
        followers: Int,
        providerId: String,
        oauthToken: String,
        oauthSecret: String,
        targetIds: List<Int>
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
                    oauthSecret = oauthSecret,
                    targetIds = targetIds
                )
            }
        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }

    fun reconnectAccount(
        tokenManager: TokenManager,
        accountId: Int,
        name: String,
        imageUri: String,
        followers: Int,
        oauthToken: String,
        oauthSecret: String
    ): MutableLiveData<ApiResponse<List<TwitterAccount>>> {
        val apiService = RetrofitBuilder.createServiceWithAuth(tokenManager)
        val requestHandler = object : RequestHandler<List<TwitterAccount>>() {
            override fun makeRequest(): Call<List<TwitterAccount>> {
                return apiService.reconnectAccount(
                    accountId = accountId,
                    name = name,
                    imageUri = imageUri,
                    followers = followers,
                    oauthToken = oauthToken,
                    oauthSecret = oauthSecret
                )
            }

        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }

    fun getTargetData(tokenManager: TokenManager): MutableLiveData<ApiResponse<Target>> {
        val apiService = RetrofitBuilder.createServiceWithAuth(tokenManager)
        val requestHandler = object : RequestHandler<Target>() {
            override fun makeRequest(): Call<Target> {
                return apiService.getTargetData()
            }
        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }
}