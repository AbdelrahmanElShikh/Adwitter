package com.marketune.adwitter.api

import android.content.Context
import android.util.Log
import com.marketune.adwitter.models.TokenManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import okhttp3.ResponseBody



/**
 * Created By Abdel-Rahman El-Shikh 19/9/2019
 */
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

        fun <T> createServiceWithAuth(
            service: Class<T>,
            tokenManager: TokenManager,
            mContext: Context
        ): T {

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
            return newRetrofit.create(service)

        }

        fun convertErrors(response: ResponseBody?): ApiError? {
            val converter =
                retrofit!!.responseBodyConverter<ApiError>(ApiError::class.java, arrayOfNulls<Annotation>(0))
            var apiError: ApiError? = null
            try {
                apiError = converter.convert(response)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Log.e(TAG,"converErrors -> $apiError")
            return apiError
        }
    }
}