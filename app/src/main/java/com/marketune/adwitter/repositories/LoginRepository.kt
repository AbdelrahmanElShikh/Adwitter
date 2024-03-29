package com.marketune.adwitter.repositories

import androidx.lifecycle.MutableLiveData
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.api.RequestHandler
import com.marketune.adwitter.api.RetrofitBuilder
import com.marketune.adwitter.models.AccessToken
import retrofit2.Call

/**
 * @author: Abdel-Rahman El-Shikh :) 22/9/2019
 */
class LoginRepository {
    companion object {
        private var loginRepository: LoginRepository? = null
        private val apiService = RetrofitBuilder.createService()

        fun getInstance(): LoginRepository {
            if (loginRepository == null)
                loginRepository = LoginRepository()
            return loginRepository!!
        }
    }

    fun login(
        userName: String,
        password: String,
        fcmToken: String
    ): MutableLiveData<ApiResponse<AccessToken>> {
        val requestHandler = object : RequestHandler<AccessToken>() {
            override fun makeRequest(): Call<AccessToken> {
                return apiService.login(
                    userName = userName,
                    password = password,
                    fcmToken = fcmToken
                )
            }
        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }

    fun googleSocialLogin(
        name: String,
        email: String,
        provider: String,
        providerId: String,
        fcmToken: String,
        imageUri: String
    ): MutableLiveData<ApiResponse<AccessToken>> {
        val requestHandler = object : RequestHandler<AccessToken>() {
            override fun makeRequest(): Call<AccessToken> {
                return apiService.socialLogin(
                    name = name,
                    email = email,
                    provider = provider,
                    provider_id = providerId,
                    fcmToken = fcmToken,
                    imageUri = imageUri
                )
            }

        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }

    fun socialLogin(
        name: String,
        email: String,
        provider: String,
        providerId: String,
        fcmToken: String,
        imageUri: String,
        oauthToken :String,
        oauthSecret:String,
        followers: Int
    ):MutableLiveData<ApiResponse<AccessToken>>{
        val requestHandler = object :RequestHandler<AccessToken>(){
            override fun makeRequest(): Call<AccessToken> {
                return  apiService.socialLogin(
                    name = name,
                    email = email,
                    provider = provider,
                    provider_id = providerId,
                    fcmToken = fcmToken,
                    imageUri = imageUri,
                    oauthToken =  oauthToken,
                    oauthSecret = oauthSecret,
                    followers = followers
                )
            }

        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }
}