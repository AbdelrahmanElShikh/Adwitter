package com.marketune.adwitter.models

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

/**
 * @author : Abdel-Rahman El-Shikh 18/9/2019
 * ............Class Description ->...........
 * ...........................................
 * This class is Responsible for managing and storing the received access token from backend
 * to the SharedPreferences and give us the ability to delete it and access it
 * ***************************************************************************
 * TokenManager is a -> @Singleton -> Class
 */
class TokenManager private constructor(private var prefs: SharedPreferences) {
    private var editor: SharedPreferences.Editor = prefs.edit()

    companion object {

        private var instance: TokenManager? = null

        @Synchronized
        fun getInstance(context: Context?): TokenManager {
            val prefs = context!!.getSharedPreferences("prefs", MODE_PRIVATE)
            if (instance == null) {
                instance = TokenManager(prefs)
            }
            return instance as TokenManager
        }
    }

    fun saveToken(token: AccessToken?) {
        editor.putString("ACCESS_TOKEN", token?.access_token).commit()
    }

    fun deleteToken() {
        editor.remove("ACCESS_TOKEN").commit()
    }

    fun getToken(): AccessToken {
        val accessToken = prefs.getString("ACCESS_TOKEN", null)
        return AccessToken(access_token = accessToken)

    }
}