package com.marketune.adwitter.api

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Response

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/22/2019
 */
abstract class RequestHandler<T> {
    private var apiResponseLiveData: MutableLiveData<ApiResponse<T>> = MutableLiveData()
    abstract fun makeRequest(): Call<T>
    fun doRequest() {
        val apiResponse: ApiResponse<T> = ApiResponse()
        makeRequest().enqueue(object : ApiCallBack<T>() {
            override fun handleResponseData(body: T?) {
                apiResponse.data = body
                apiResponse.status = Status.SUCCESS
                apiResponseLiveData.value = apiResponse
            }

            override fun handleError(response: Response<T>) {
                apiResponse.apiError = RetrofitBuilder.convertErrors(response  = response.errorBody())
                apiResponse.status = Status.ERROR
                apiResponseLiveData.value = apiResponse
            }

            override fun handleException(exception: Exception) {
                apiResponse.apiException = exception
                apiResponse.status = Status.FAILURE
                apiResponseLiveData.value = apiResponse
            }
        })
    }
    fun getApiResponse():MutableLiveData<ApiResponse<T>>{
        return apiResponseLiveData
    }
}