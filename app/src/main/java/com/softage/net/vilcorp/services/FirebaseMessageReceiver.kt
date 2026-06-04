package com.softage.net.vilcorp.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.softage.net.vilcorp.R

import com.softage.net.vilcorp.ui.activity.MainActivity
import com.softage.net.vilcorp.util.Utility
import org.json.JSONObject
import java.util.*

class FirebaseMessageReceiver : FirebaseMessagingService() {

    private lateinit var mUtil: Utility

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("FCM_TOKEN", token)

        getSharedPreferences("_", MODE_PRIVATE)
            .edit()
            .putString("fcm_token", token)
            .apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage.data

        if (data.isNotEmpty()) {

            val title = data["title"] ?: ""
            val body = data["body"] ?: ""

            Log.d("FCM_DATA", data.toString())

            mUtil = Utility(this)
//            mUtil.setFcmTitle(title)
//            mUtil.setFcmBody(body)

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("title", title)
                putExtra("body", body)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            sendNotification(title, body, pendingIntent)
        }
    }

    companion object {
        fun getToken(context: Context): String {
            return context.getSharedPreferences("_", MODE_PRIVATE)
                .getString("fcm_token", "empty") ?: "empty"
        }
    }

    private fun sendNotification(
        title: String,
        body: String,
        pendingIntent: PendingIntent
    ) {

        val channelId = "srot.notification.channel"

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(Html.fromHtml(title.trim(), Html.FROM_HTML_MODE_LEGACY))
            .setContentText(Html.fromHtml(body.trim(), Html.FROM_HTML_MODE_LEGACY))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                channelId,
                "Srot Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}