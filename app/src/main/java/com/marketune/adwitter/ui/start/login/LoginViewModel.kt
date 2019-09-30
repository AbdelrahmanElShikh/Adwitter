package com.marketune.adwitter.ui.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.models.AccessToken
import com.marketune.adwitter.repositories.LoginRepository

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/22/2019
 */
class LoginViewModel : ViewModel() {
    private lateinit var instance : LoginRepository
    private lateinit var response: MutableLiveData<ApiResponse<AccessToken>>

    fun init(){
        instance = LoginRepository.getInstance()
    }

    fun login(userName: String, password: String, fcmToken: String) : LiveData<ApiResponse<AccessToken>> {
        response = instance.login(userName = userName, password = password,fcmToken = fcmToken)
        return response
    }

    fun googleSocialLogin(
        name: String,
        email: String,
        provider: String,
        providerId: String,
        fcmToken: String,
        imageUri: String
    ):LiveData<ApiResponse<AccessToken>>{
        response = instance.googleSocialLogin(
            name = name,
            email = email,
            provider = provider,
            providerId = providerId,
            fcmToken = fcmToken,
            imageUri = imageUri
        )
        return response
    }
    fun socialLogin(name: String,
                    email: String,
                    provider: String,
                    providerId: String,
                    fcmToken: String,
                    imageUri: String,
                    oauthToken :String,
                    oauthSecret:String,
                    followers: Int
    ):LiveData<ApiResponse<AccessToken>>{
        response = instance.socialLogin(
            name = name,
            email = email,
            provider = provider,
            providerId = providerId,
            fcmToken = fcmToken,
            imageUri = imageUri,
            oauthToken = oauthToken,
            oauthSecret = oauthSecret,
            followers = followers
            )
        return response
    }


}