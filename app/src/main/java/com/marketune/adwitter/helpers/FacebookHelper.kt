package com.marketune.adwitter.helpers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.marketune.adwitter.models.FacebookUser
import org.json.JSONObject

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/3/2019
 */
class FacebookHelper constructor(
    private var mListener: FacebookResponse,
    private var mFieldString: String
) {
    private var mCallbackManager: CallbackManager = CallbackManager.Factory.create()

    init {
        val mCallback = object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                getUserProfile(result)
            }

            override fun onCancel() {
                mListener.onFbSignInFail()
            }

            override fun onError(error: FacebookException?) {
                mListener.onFbSignInFail()
            }

        }
        LoginManager.getInstance().registerCallback(mCallbackManager, mCallback)
    }

    private fun getUserProfile(result: LoginResult?) {
        val request = GraphRequest.newMeRequest(
            result!!.accessToken
        ) { jsonObject, response ->
            Log.e("response: ", response.toString() + "")
            try {
                mListener.onFbProfileReceived(parseResponse(jsonObject))
            } catch (e: Exception) {
                e.printStackTrace()
                mListener.onFbSignInFail()
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", mFieldString)
        request.parameters = parameters
        request.executeAsync()
    }

    private fun parseResponse(jsonObject: JSONObject?): FacebookUser {
        val user = FacebookUser()
        if (jsonObject != null) {
            user.response = jsonObject
            if (jsonObject.has("id")) user.facebookId = jsonObject.getString("id")
            if (jsonObject.has("email")) user.email = jsonObject.getString("email")
            if (jsonObject.has("name")) user.name = jsonObject.getString("name")
            if (jsonObject.has("gender")) user.gender = jsonObject.getString("gender")
            if (jsonObject.has("about")) user.about= jsonObject.getString("about")
            if (jsonObject.has("bio")) user.bio= jsonObject.getString("bio")
            if (jsonObject.has("cover")) user.coverPic= jsonObject.getJSONObject("cover").getString("source")
            if (jsonObject.has("picture")) user.profilePic= jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")
        }
        return user

    }

     fun performSignIn(activity: Activity){
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("public_profile", "email"))
    }

    fun onActivityResult(requestCode:Int,resultCode:Int,data: Intent?){
        mCallbackManager.onActivityResult(requestCode,resultCode,data)
    }

    interface FacebookResponse {
        fun onFbSignInFail()
        fun onFbProfileReceived(user: FacebookUser)
    }
}