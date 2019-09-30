package com.marketune.adwitter.models

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/30/2019
 */
class TwitterUser(
    var id: String = "",
    var useName: String = "",
    var name: String = "",
    var email: String = "",
    var descripton: String = "",
    var profileImageUrl: String = "",
    var bannerUrl: String = "",
    var language: String = "",
    var token: String = "",
    var secret: String = "",
    var followersCount: Int = 0
)
