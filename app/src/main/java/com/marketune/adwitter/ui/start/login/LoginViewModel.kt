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
}