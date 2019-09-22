package com.marketune.adwitter.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/22/2019
 */
abstract class ApiCallBack<T> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            handleResponseData(response.body())
        } else {
            handleError(response)
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        if (t is Exception)
            handleException(t)
    }

    abstract fun handleResponseData(body: T?)
    abstract fun handleError(response: Response<T>)
    abstract fun handleException(exception: Exception)


}
