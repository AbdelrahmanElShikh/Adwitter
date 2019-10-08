package com.marketune.adwitter.models

import com.google.gson.annotations.SerializedName

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/22/2019
 */
data class TwitterAccount(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("followers") val followers: Int,
    @SerializedName("profile_type") val profile_type: String,
    @SerializedName("provider_id") val provider_id: Long,
    @SerializedName("oauth_token") val oauth_token: String,
    @SerializedName("oauth_secret") val oauth_secret: String,
    @SerializedName("status") val status: Boolean,
    @SerializedName("connected") val connected: Boolean,
    @SerializedName("user_id") val user_id: Int
)
