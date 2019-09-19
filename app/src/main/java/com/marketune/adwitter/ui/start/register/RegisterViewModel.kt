package com.marketune.adwitter.ui.start.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.models.AccessToken
import com.marketune.adwitter.repositories.RegisterRepository

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh on 9/18/2019
 */
class RegisterViewModel : ViewModel() {
    private lateinit var instance : RegisterRepository
    private lateinit var response: MutableLiveData<ApiResponse<AccessToken>>

    fun init(){
        instance = RegisterRepository.getInstance()
    }

    fun registerNewUser(name: String, email: String, password: String, fcmToken: String) : LiveData<ApiResponse<AccessToken>>{
         response = instance.registerNewUser(name = name, email = email, password = password,fcmToken = fcmToken)
        return response
    }


}