package com.marketune.adwitter.models

import com.google.gson.annotations.SerializedName

data class Details(
    @SerializedName("account_number") var account_number: String,
    @SerializedName("bank_country") var bank_country: String,
    @SerializedName("bank_name") var bank_name: String,
    @SerializedName("beneficiary_name") var beneficiary_name: String,
    @SerializedName("iban") var iban: String,
    @SerializedName("id") var id: Int,
    @SerializedName("phone") var phone: String,
    @SerializedName("user_id") var user_id: Int
)