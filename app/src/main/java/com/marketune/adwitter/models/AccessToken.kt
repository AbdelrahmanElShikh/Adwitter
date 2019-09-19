package com.marketune.adwitter.models

import com.google.gson.annotations.SerializedName

/**
 * Created By Abdel-Rahman El-Shikh 17/9/2019
 */
data class AccessToken(
    @SerializedName("access_token") val access_token : String?
)