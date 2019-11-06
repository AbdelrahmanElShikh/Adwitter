package com.marketune.adwitter.models

import com.google.gson.annotations.SerializedName

data class Details(
    @SerializedName("account_number") val account_number: String,
    @SerializedName("bank_country") val bank_country: String,
    @SerializedName("bank_name") val bank_name: String,
    @SerializedName("beneficiary_name") val beneficiary_name: String,
    @SerializedName("iban") val iban: String,
    @SerializedName("id") val id: Int,
    @SerializedName("phone") val phone: String,
    @SerializedName("user_id") val user_id: Int
)