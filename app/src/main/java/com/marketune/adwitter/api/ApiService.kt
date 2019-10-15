package com.marketune.adwitter.api

import com.marketune.adwitter.models.AccessToken
import com.marketune.adwitter.models.Target
import com.marketune.adwitter.models.TwitterAccount
import com.marketune.adwitter.models.User
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    //For Google AccessToken
    @POST("social_auth")
    @FormUrlEncoded
    fun socialLogin(@Field("name") name: String,
                    @Field("email") email: String,
                    @Field("provider")provider:String,
                    @Field("provider_id") provider_id: String,
                    @Field("fcm_token") fcmToken: String,
                    @Field("avatar") imageUri: String
    ):Call<AccessToken>

    //For twitter or facebook AccessToken
    @POST("social_auth")
    @FormUrlEncoded
    fun socialLogin(@Field("name") name: String,
                    @Field("email") email: String,
                    @Field("provider")provider:String,
                    @Field("provider_id") provider_id: String,
                    @Field("fcm_token") fcmToken: String,
                    @Field("avatar") imageUri: String,
                    @Field("oauth_token") oauthToken :String,
                    @Field("oauth_secret") oauthSecret:String,
                    @Field("followers")followers: Int
    ):Call<AccessToken>

    //get User Info
    @GET("user")
    fun getUserInfo():Call<User>

    //get user TwitterAccountList
    @GET("account")
    fun getTwitterAccounts():Call<List<TwitterAccount>>

    //add twitterAccount for the user
    @POST("account")
    @FormUrlEncoded
    fun addAccount(
        @Field("name") name:String,
        @Field("avatar") imageUri: String,
        @Field("followers") followers: Int,
        @Field("provider_id") providerId: String,
        @Field("oauth_token") oauthToken:String,
        @Field("oauth_secret") oauthSecret:String,
        @Field("target_ids") targetIds:List<Int>
    ):Call<List<TwitterAccount>>

    //Get target data
    @GET("target")
    fun getTargetData():Call<Target>

}