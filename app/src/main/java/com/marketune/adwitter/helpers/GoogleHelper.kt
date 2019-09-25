package com.marketune.adwitter.helpers

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.marketune.adwitter.models.GoogleUser

/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 9/25/2019
 */
class GoogleHelper constructor(var mContext: Context, var mListener: GoogleAuthResponse?) {
    private lateinit var googleSignInClient: GoogleSignInClient
    init {
        if(mListener == null){
            throw RuntimeException("GoogleAuthResponse listener cannot be null.")
        }
        buildGoogleSignInClient(buildSignInOptions())
    }

    private fun buildSignInOptions(): GoogleSignInOptions {
        val signInOptions =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestId()
        /**
         *TODO :signInOptions.requestIdToken(GoogleKeys.GOOGLE_SERVER_CLIENT_ID);
         * If your app authenticates with a backend server or accesses Google APIs from your backend server,
         * you must get the OAuth 2.0 client ID that was created for your server.
         */
            return signInOptions.build()
    }

    private fun buildGoogleSignInClient(signInOptions: GoogleSignInOptions) {
        googleSignInClient =GoogleSignIn.getClient(mContext,signInOptions)
    }

    interface GoogleAuthResponse {
        fun onGoogleAuthSignin(user: GoogleUser)
        fun onGoogleAuthSignInFailed()
    }

}