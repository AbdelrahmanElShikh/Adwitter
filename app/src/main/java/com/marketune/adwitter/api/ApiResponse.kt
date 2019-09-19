package com.marketune.adwitter.api

import kotlin.Exception


/**
 * @author: By Abdel-Rahman El-Shikh 18/9/2019
 */
class ApiResponse<T> (
     var apiException: Exception? = null,
     var data : T ?= null,
     var apiError : ApiError? = null,
     var status: Status? = null
)