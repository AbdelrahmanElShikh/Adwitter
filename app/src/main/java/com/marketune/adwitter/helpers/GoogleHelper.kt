package com.marketune.adwitter.helpers

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.marketune.adwitter.R
import com.marketune.adwitter.models.GoogleUser

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/25/2019
 */
private const val TAG = "GoogleHelper"
private const val RC_SIGN_IN = 500

class GoogleHelper constructor(
    private var mContext: FragmentActivity,
    private val fragmentContext : Fragment,
    private var mListener: GoogleAuthResponse
) {
    private lateinit var googleSignInClient: GoogleSignInClient

    init {
        buildGoogleSignInClient(buildSignInOptions())
    }

    private fun buildSignInOptions(): GoogleSignInOptions {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()
            .requestIdToken(mContext.getString(R.string.GOOGLE_SERVER_CLIENT_ID))
        /**
         *TODO :signInOptions.requestIdToken(GoogleKeys.GOOGLE_SERVER_CLIENT_ID);
         * If your app authenticates with a backend server or accesses Google APIs from your backend server,
         * you must get the OAuth 2.0 client ID that was created for your server.
         */
        return signInOptions.build()
    }

    private fun buildGoogleSignInClient(signInOptions: GoogleSignInOptions) {
        googleSignInClient = GoogleSignIn.getClient(mContext, signInOptions)
    }

    fun performSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        fragmentContext.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            } else {
                mListener.onGoogleAuthSignInFailed()
            }
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        try {
            val googleSignInAccount = task!!.getResult(ApiException::class.java)
            mListener.onGoogleAuthSignIn(parseToGoogleUser(googleSignInAccount!!))
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode + e.message)
        }
    }

    private fun parseToGoogleUser(account: GoogleSignInAccount): GoogleUser {
        val user = GoogleUser()
        user.id = account.id!!
        user.name = account.displayName!!
        user.email = account.email!!
        user.idToken = account.idToken!!
        user.familyName = account.familyName!!
        user.photoUri = account.photoUrl!!
        return user

    }


    interface GoogleAuthResponse {
        fun onGoogleAuthSignIn(user: GoogleUser)
        fun onGoogleAuthSignInFailed()
    }

}