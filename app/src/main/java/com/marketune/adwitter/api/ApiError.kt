package com.marketune.adwitter.api

/**
 * @author: Abdel-Rahman El-Shikh 18/9/2019
 */
class ApiError(
     val code: Int,
     val message : String,
     val email : List<String>,
     val errors: Map<String, List<String>> = mapOf()
    )
