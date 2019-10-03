package com.marketune.adwitter.models

import org.json.JSONObject

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/3/2019
 */
class FacebookUser(
    var name:String="",
    var email:String="",
    var facebookId:String="",
    var gender:String="",
    var about :String="",
    var profilePic:String="",
    var bio:String="",
    var coverPic: String="",
    var response : JSONObject=JSONObject()
)