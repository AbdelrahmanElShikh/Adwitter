package com.marketune.adwitter.helpers

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.marketune.adwitter.R
import com.marketune.adwitter.models.TwitterUser
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User


/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/30/2019
 */
private const val TAG = "TwitterHelper"

class TwitterHelper constructor(
    private var mContext: Activity,
    private var mListener: TwitterAuthResponse,
    twitterApiKey: String,
    twitterSecretKey: String

) {
    private var mAuthClient: TwitterAuthClient
    init {
        val authConfig = TwitterConfig.Builder(mContext)
            .logger(DefaultLogger(Log.DEBUG))
            .twitterAuthConfig(TwitterAuthConfig(twitterApiKey, twitterSecretKey))
            .debug(true)
            .build()
        Twitter.initialize(authConfig)
        mAuthClient = TwitterAuthClient()
    }


    fun authorize() {
        mAuthClient.authorize(mContext, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                getUserData()
            }

            override fun failure(exception: TwitterException?) {
                mListener.onTwitterError()
            }
        })
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mAuthClient.onActivityResult(requestCode, resultCode, data)

    }

    private fun getUserData() {
        val session = TwitterCore.getInstance().sessionManager.activeSession
        val authToken = session.authToken
        val apiClient = TwitterCore.getInstance().apiClient
        val accountService = apiClient.accountService
        val call = accountService.verifyCredentials(true, true, true)
        call.enqueue(object : Callback<User>() {
            override fun success(result: Result<User>?) {
                val user = TwitterUser()
                val resultInfo = result!!.data
                user.id = resultInfo.id.toString()
                user.name = resultInfo.name
                user.useName = resultInfo.screenName
                user.email = resultInfo.email
                user.descripton = resultInfo.description
                user.profileImageUrl = resultInfo.profileImageUrl.replace(
                    mContext.getString(R.string.normal),
                    mContext.getString(R.string.twitter_image_size)
                )
                user.token = authToken.token
                user.secret = authToken.secret
                user.followersCount = resultInfo.followersCount
                mListener.onTwitterProfileReceived(user)
            }

            override fun failure(exception: TwitterException?) {
                Log.e(TAG, "Cant retrieve account with message :" + exception!!.localizedMessage)
            }
        })
    }
}

interface TwitterAuthResponse {
    fun onTwitterError()
    fun onTwitterProfileReceived(user: TwitterUser)
}
