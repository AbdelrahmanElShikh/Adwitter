package com.marketune.adwitter.models

import com.google.gson.annotations.SerializedName

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/8/2019
 */
class StringResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("code") val code: Int?
)