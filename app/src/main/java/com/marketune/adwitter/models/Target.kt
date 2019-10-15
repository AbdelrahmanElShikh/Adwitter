package com.marketune.adwitter.models

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/14/2019
 */
data class Target(
    val age: List<TargetResponse>,
    val area: List<TargetResponse>,
    val interest: List<TargetResponse>
)

data class TargetResponse(
    val id: Int,
    val key: String,
    val langs: Langs,
    val type: String
)

data class Langs(
    val ar: String,
    val en: String
)
