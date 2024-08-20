package com.example.chatappnative.helper

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import com.example.chatappnative.R
import javax.inject.Inject

class LocalNotificationHelper @Inject constructor(
    private val context: Context,
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(contextValue: Context?, title: String, body: String) {
        val notification = Notification.Builder(contextValue ?: context)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(body)

            .build()

        notificationManager.notify(1, notification)
    }
}