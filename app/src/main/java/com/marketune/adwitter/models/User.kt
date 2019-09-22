package com.marketune.adwitter.models

import com.google.gson.annotations.SerializedName

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/22/2019
 */
data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("balance") val balance: Double
)