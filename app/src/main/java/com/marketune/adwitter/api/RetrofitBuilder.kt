package com.marketune.adwitter.api

import android.util.Log
import com.marketune.adwitter.models.TokenManager
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


/**
 * @author: Abdel-Rahman El-Shikh :) 19/9/2019
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RetrofitBuilder {
    companion object {
        private const val BASE_URL = "http://192.168.1.10/adw/public/api/"
        private val client = buildClient()
        private var retrofit: Retrofit? = getClient()
        private val TAG = "RetrofitBuilder"


        private fun getClient(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

        private fun buildClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    var request = chain.request()
                    Log.e(TAG, "intercept: $BASE_URL")
                    val builder = request.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Connection", "close")

                    request = builder.build()

                    chain.proceed(request)
                }

            return builder.build()

        }

        fun createService(): ApiService {
            return retrofit!!.create(ApiService::class.java)
        }

        fun  createServiceWithAuth(tokenManager: TokenManager): ApiService {

            val newClient = client.newBuilder()
                .addInterceptor { chain ->
                    var request = chain.request()

                    val builder = request.newBuilder()

                    if (tokenManager.getToken().access_token != null) {
                        builder.addHeader(
                            "Authorization",
                            "Bearer " + tokenManager.getToken().access_token
                        )
                    }
                    request = builder.build()
                    chain.proceed(request)
                }.build()

            val newRetrofit = retrofit!!.newBuilder().client(newClient).build()
            return newRetrofit.create(ApiService::class.java)

        }

        fun convertErrors(response: ResponseBody?): ApiError? {
            val converter =
                retrofit!!.responseBodyConverter<ApiError>(
                    ApiError::class.java,
                    arrayOfNulls<Annotation>(0)
                )
            var apiError: ApiError? = null
            try {
                apiError = converter.convert(response)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return apiError
        }
    }
}