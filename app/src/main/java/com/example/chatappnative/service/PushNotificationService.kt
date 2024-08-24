package com.example.chatappnative.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.chatappnative.R
import com.example.chatappnative.data.model.DataNotificationModel
import com.example.chatappnative.data.model.FriendStatusModel
import com.example.chatappnative.domain.repository.AuthRepository
import com.example.chatappnative.presentation.add_contact.AddContactActivity
import com.example.chatappnative.presentation.auth.login.LoginActivity
import com.example.chatappnative.presentation.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        CoroutineScope(Dispatchers.Default).launch {
            authRepository.refreshToken(token).collect {
                Log.d("PushNotificationService", "refreshToken: ${it.data?.deviceToken}")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        var pendingIntent: PendingIntent? = null
        try {
            if (remoteMessage.data.isNotEmpty()) {
                Log.d("PushNotificationService", "data: ${remoteMessage.data}")

                val jsonString = Gson().toJson(remoteMessage.data)

                val notificationData =
                    Gson().fromJson(jsonString, DataNotificationModel::class.java)

                if (notificationData.event == "add_contact") {
                    pendingIntent = handleAddContactEvent(notificationData)
                }
            }

            remoteMessage.notification?.let {
                if (it.title?.isNotEmpty() == true || it.body?.isNotEmpty() == true) {
                    generateNotification(
                        it.title ?: "",
                        it.body ?: "",
                        customPendingIntent = pendingIntent
                    )
                }
            }

        } catch (e: Exception) {
            Log.d("PushNotificationService", "onMessageReceived: ${e.message}")
        }
    }

    private fun generateNotification(
        title: String,
        body: String,
        customPendingIntent: PendingIntent? = null
    ) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
        )

        val builder = NotificationCompat.Builder(
            applicationContext,
            getString(R.string.default_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .setContentIntent(customPendingIntent ?: pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())
    }

    private fun handleAddContactEvent(notificationData: DataNotificationModel): PendingIntent? {
        val friendStatus = Gson().fromJson(
            notificationData.data.toString(),
            FriendStatusModel::class.java
        )

        Log.d("PushNotificationService", "friendStatus: $friendStatus")
        
        when (friendStatus.senderStatus) {
            1 -> {
                EventBusService.sendEvent(
                    notificationData.event,
                    friendStatus,
                )

                EventBusService.sendFriendEvent(
                    friendStatus.senderStatus,
                    friendStatus.friendInfo,
                )

                return null
            }

            2 -> {
                EventBusService.sendEvent(
                    notificationData.event,
                    friendStatus,
                )

                val intent = Intent(this, AddContactActivity::class.java)

                return PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
                )
            }

            3 -> {
                EventBusService.sendEvent(
                    notificationData.event,
                    friendStatus,
                )

                EventBusService.sendFriendEvent(
                    friendStatus.senderStatus,
                    friendStatus.friendInfo,
                )

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(MainActivity.TAB_INDEX, 1)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                return PendingIntent.getActivity(
                    this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE,
                )
            }

            else -> return null
        }
    }
}