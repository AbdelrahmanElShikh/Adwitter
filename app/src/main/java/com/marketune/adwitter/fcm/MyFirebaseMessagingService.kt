package com.marketune.adwitter.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created By Abdel-Rahman El-Shikh 17/9/2019
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val message : Map<String,String> = remoteMessage.data
        val title= message.get("title")
        val body = message.get("body")
        NotificationUtils.notifyUser(this, title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("FCMService","onNewToken :  $token")
    }
}