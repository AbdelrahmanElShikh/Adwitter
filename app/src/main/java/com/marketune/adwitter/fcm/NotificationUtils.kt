package com.marketune.adwitter.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.marketune.adwitter.R
import com.marketune.adwitter.ui.main.MainActivity

/**
 * Created By Abdel-Rahman El-Shikh 17/9/2019
 */
class NotificationUtils {

    /**
     * Companion Object in kotlin means static in java .. Fahmni ya eli bt2ra ?
     */
    companion object{

        private const val CHANNEL_ID = "message_notification_channel"
        private const val ADWITTER_MESSAGE_NOTIFICATION_ID = 1138
        private const val ADWITTER_MESSAGE_PENDING_INTENT_ID = 3417

        fun notifyUser(context: Context,title: String?, body: String?){

            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            /**
             * This if statement block is so important as it creates notification channel for android devices above android Oreo.
             * and we can't send notification to these devices without this channel and any other device (below android Oreo)
             * will ignore the channel parameter.(Abdel-Rahman)
             */
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = NotificationChannel(CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                    )
                notificationManager.createNotificationChannel(channel)
            }


            /**
             * Here we are building the notification and setting it's content
             *  PARAM -> (CHANNEL_ID) Will be ignored with devices below Android Oreo
             */
            //TODO : Please Note that adwitter logo is not shown properly.
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_home)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


            notificationManager.notify(ADWITTER_MESSAGE_NOTIFICATION_ID,builder.build())
        }

        /**
         * This Method responsible for what happens when user interact th the Notification.
         * here i'm sending him to the MainActivity
         */
        private fun contentIntent(context: Context):PendingIntent{
            val startActivity =Intent(context,MainActivity::class.java)
            return PendingIntent.getActivity(context,ADWITTER_MESSAGE_PENDING_INTENT_ID,startActivity,PendingIntent.FLAG_UPDATE_CURRENT)
        }
        private fun largeIcon(context: Context): Bitmap {
            val res = context.resources
            return BitmapFactory.decodeResource(res, R.drawable.ic_adwitter_icon_white)
        }
    }



}