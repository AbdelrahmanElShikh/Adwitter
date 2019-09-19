package com.marketune.adwitter.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.marketune.adwitter.api.ApiResponse
import com.marketune.adwitter.api.RetrofitBuilder
import com.marketune.adwitter.models.AccessToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.marketune.adwitter.api.Status
import java.lang.Exception


/**
 * @author: Abdel-Rahman El-Shikh 18/9/2019
 * i will pardon this repo from using request handler class just
 * for understanding purpose
 */
class RegisterRepository {
    private var apiResponse: ApiResponse<AccessToken> = ApiResponse()

    companion object {
        private var registerRepository: RegisterRepository? = null
        private val apiService = RetrofitBuilder.createService()

        fun getInstance(): RegisterRepository {
            if (registerRepository == null)
                registerRepository = RegisterRepository()
            return registerRepository!!
        }
    }

    fun registerNewUser(
        name: String,
        email: String,
        password: String,
        fcmToken: String
    ): MutableLiveData<ApiResponse<AccessToken>> {

        val registerResponse: MutableLiveData<ApiResponse<AccessToken>> = MutableLiveData()
        (apiService.register(name, email, password, fcmToken)).enqueue(object :
            Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    apiResponse.status = Status.SUCCESS
                    apiResponse.data = response.body()
                    registerResponse.value = apiResponse
                } else {
                    apiResponse.status = Status.ERROR
                    apiResponse.apiError = RetrofitBuilder.convertErrors(response.errorBody())
                    registerResponse.value = apiResponse
                }

            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                apiResponse.status = Status.FAILURE
                apiResponse.apiException = t as Exception
                registerResponse.value = apiResponse
                Log.e(
                    "Repo",
                    "onFailure - > : ${registerResponse.value!!.apiException!!.localizedMessage}"
                )

            }
        })
        return registerResponse

    }


}