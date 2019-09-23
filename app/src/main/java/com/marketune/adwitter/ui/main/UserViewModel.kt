package com.marketune.adwitter.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.models.TokenManager
import com.marketune.adwitter.models.User
import com.marketune.adwitter.repositories.UserRepository

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/23/2019
 */
class UserViewModel : ViewModel() {
    private lateinit var instance : UserRepository
    private lateinit var response: MutableLiveData<ApiResponse<User>>
    fun init(){
        instance = UserRepository.getInstance()
    }
    fun getUserInfo(tokenManager: TokenManager):LiveData<ApiResponse<User>>{
        response =  instance.getUserInfo(tokenManager)
        return response
    }


}