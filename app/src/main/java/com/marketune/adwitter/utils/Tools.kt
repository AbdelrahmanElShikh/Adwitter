package com.marketune.adwitter.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


/**
 * Adwitter
 * @author: Abdel-Rahman El-Shikh :) on 10/27/2019
 */
class Tools {
    companion object{
        fun checkInternetConnection(context: Context): Boolean {
            val connectivity =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (connectivity == null) {
                return false
            } else {
                val info = connectivity.allNetworkInfo
                if (info != null) {
                    for (networkInfo in info) {
                        if (networkInfo.state == NetworkInfo.State.CONNECTED) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    }
}