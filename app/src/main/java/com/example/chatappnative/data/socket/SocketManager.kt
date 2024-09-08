package com.example.chatappnative.data.socket

import android.util.Log
import com.example.chatappnative.core.constants.NetworkUrl
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.event.UpdateSentMessageEvent
import com.example.chatappnative.service.EventBusService
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URI
import java.net.URISyntaxException

class SocketManager(
    private val preferences: Preferences
) {
    var socket: Socket? = null
    private var needReconnect = false

    fun connect() {
        val options = IO.Options.builder().build()

        try {
            val uri: URI = URI.create(NetworkUrl.BASE_URL)

            socket = IO.socket(
                uri,
                options,
            )

            socket?.connect()

            onConnect {
                Log.d("SocketManager", "socket connection: ${socket?.connected()}")

                onUserPresence()
                onUserPresenceDisconnect()
                onUpdateSentMessages()
                onNewMessage()
            }

        } catch (e: URISyntaxException) {
            Log.d("SocketManager", "connect socket error: $e")
            e.printStackTrace()
        }
    }

    fun onConnect(action: () -> Unit) {
        socket?.on(Socket.EVENT_CONNECT) {
            action()
        }
    }

    fun emitLoggedInEvent() {
        Log.d("SocketManager", "emitLoggedInEvent")
        val data = JSONObject()
        data.put("access_token", preferences.getAccessToken())

        socket?.emit("LoggedIn", data)

    }

    private fun onUserPresence() {
        socket?.on("updateUserPresence") {
            val data = it[0] as JSONObject

            Log.d("SocketManager", "onUserPresence: {$data}")

            val userPresence = Gson().fromJson(data.toString(), UserPresenceSocketModel::class.java)

            EventBusService.sendUpdateUserPresenceEvent(userPresence)
        }
    }

    private fun onUserPresenceDisconnect() {
        socket?.on("updateUserPresenceDisconnect") {
            val data = it[0] as JSONObject

            Log.d("SocketManager", "onUserPresenceDisconnect: {$data}")

            val userPresence = Gson().fromJson(data.toString(), UserPresenceSocketModel::class.java)

            EventBusService.sendUpdateUserPresenceEvent(userPresence)
        }
    }

    fun emitClientSendMessage(
        message: MessageModel,
        chatID: String,
        userId: String,
    ) {
        // Convert MessageModel to JSON string using Gson
        val jsonString = Gson().toJson(message)

        // Parse JSON string into JSONObject
        val jsonObject = JSONObject(jsonString)

        jsonObject.put("chatID", chatID)
        jsonObject.put("userID", userId)
        Log.d("SocketManager", "emitClientSendMessage: $jsonObject")
        socket?.emit("clientSendMessage", jsonObject)
    }

    fun joinRoom(chatID: String) {
        Log.d("SocketManager", "joinRoom: $chatID")
        val data = JSONObject()
        data.put("chatID", chatID)

        socket?.emit("joinRoom", chatID)
    }

    fun leaveRoom(chatID: String) {
        Log.d("SocketManager", "leaveRoom: $chatID")
        val data = JSONObject()
        data.put("chatID", chatID)

        socket?.emit("leaveRoom", chatID)
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun emitReconnect() {
        if (needReconnect) return

        val data = JSONObject()
        data.put("access_token", preferences.getAccessToken())

        Log.d("SocketManager", "reconnect: {$data}")

        socket?.emit("reconnect", data)

        needReconnect = true
    }

    fun updateNeedReconnect(value: Boolean) {
        needReconnect = value
    }

    private fun onUpdateSentMessages() {
        socket?.on("updateSentMessages") {
            val data = it[0] as JSONObject

            Log.d("SocketManager", "onUpdateSentMessages: {$data}")

            val sentMessageEvent =
                Gson().fromJson(data.toString(), UpdateSentMessageEvent::class.java)

            EventBusService.sendUpdateSentMessageEvent(sentMessageEvent)
        }
    }

    private fun onNewMessage() {
        socket?.on("newMessage") {
            val data = it[0] as JSONObject

            Log.d("SocketManager", "onNewMessage: {$data}")

            val newMessage =
                Gson().fromJson(data.toString(), MessageModel::class.java)

            EventBusService.sendNewMessageEvent(newMessage)
        }
    }

    fun onDisconnect(action: () -> Unit) {
        socket?.on(Socket.EVENT_DISCONNECT) {
            action()
        }
    }
}