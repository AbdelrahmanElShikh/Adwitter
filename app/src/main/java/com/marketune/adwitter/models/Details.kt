package com.marketune.adwitter.models

data class Details(
    val account_number: String,
    val bank_country: String,
    val bank_name: String,
    val beneficiary_name: String,
    val created_at: String,
    val iban: String,
    val id: Int,
    val phone: String,
    val updated_at: String,
    val user_id: Int
)