package com.marketune.adwitter.models

import android.net.Uri

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/25/2019
 */
class GoogleUser(
    var id:String,
    var name:String,
    var email:String,
    var idToken:String,
    var familyName:String,
    var photoUri:Uri
                 )
