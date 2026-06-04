package com.softage.net.vilcorp.util

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity

object NetworkHelper : AppCompatActivity() {
    fun isConnected(mContext: Context): Boolean {
        var connected = false
        try {
            val cm = mContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
        }
        return connected
    }
}
