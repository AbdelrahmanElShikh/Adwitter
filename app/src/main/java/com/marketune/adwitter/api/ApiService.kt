package com.marketune.adwitter.api

import com.marketune.adwitter.models.AccessToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author: Abdel-Rahman El-Shikh :) 17/9/2019
 */
interface ApiService {

    @POST("register")
    @FormUrlEncoded
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("fcm_token") fcmToken: String
    ): Call<AccessToken>

    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("fcm_token") fcmToken: String
    ):Call<AccessToken>
}