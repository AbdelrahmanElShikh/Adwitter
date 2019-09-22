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
        val requestHandler = object : RequestHandler<AccessToken>(){
            override fun makeRequest(): Call<AccessToken> {
                return apiService.login(userName = userName,password = password,fcmToken = fcmToken)
            }
        }
        requestHandler.doRequest()
        return requestHandler.getApiResponse()
    }
}