package com.example.chatappnative.data.socket

import android.util.Log
import com.example.chatappnative.core.constants.NetworkUrl
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.data.param.UserTypingParam
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

            Log.d("SocketManager", "onUserPresence: $data")

            val userPresence = Gson().fromJson(data.toString(), UserPresenceSocketModel::class.java)

            EventBusService.sendUpdateUserPresenceEvent(userPresence)
        }
    }

    private fun onUserPresenceDisconnect() {
        socket?.on("updateUserPresenceDisconnect") {
            val data = it[0] as JSONObject

            Log.d("SocketManager", "onUserPresenceDisconnect: $data")

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

        socket?.emit("joinRoom", data)
    }

    fun leaveRoom(chatID: String) {
        Log.d("SocketManager", "leaveRoom: $chatID")
        val data = JSONObject()
        data.put("chatID", chatID)

        socket?.emit("leaveRoom", data)
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun emitReconnect() {
        if (needReconnect) return

        val data = JSONObject()
        data.put("access_token", preferences.getAccessToken())

        Log.d("SocketManager", "reconnect: $data")

        socket?.emit("reconnect", data)

        needReconnect = true
    }

    fun updateNeedReconnect(value: Boolean) {
        needReconnect = value
    }

    fun onUpdateSentMessages(action: (UpdateSentMessageEvent) -> Unit) {
        socket?.on("updateSentMessages") {
            val data = it[0] as JSONObject

            Log.d("SocketManager", "onUpdateSentMessages: $data")

            val sentMessageEvent =
                Gson().fromJson(data.toString(), UpdateSentMessageEvent::class.java)

            action(sentMessageEvent)
        }
    }

    fun onNewMessage(action: (newMessage: MessageModel) -> Unit) {
        socket?.on("newMessage") {
            val data = it[0] as JSONObject

            Log.d("SocketManager", "onNewMessage: $data")

            val newMessage =
                Gson().fromJson(data.toString(), MessageModel::class.java)

            action(newMessage)
        }
    }

    fun emitUpdateReadMessages(chatID: String) {
        val data = JSONObject()
        data.put("chatID", chatID)

        Log.d("SocketManager", "emitUpdateReadMessages: $data")

        socket?.emit("updateReadMessages", data)
    }

    fun onUserReadMessages(action: (chatID: String) -> Unit) {
        socket?.on("userReadMessages") {
            val data = it[0] as JSONObject

            Log.d("SocketManager", "onUpdateReadMessages: $data")

            action(data.getString("chatID"))
        }
    }

    fun emitUserTyping(userTypingParam: UserTypingParam) {
        // Convert MessageModel to JSON string using Gson
        val jsonString = Gson().toJson(userTypingParam)

        // Parse JSON string into JSONObject
        val jsonObject = JSONObject(jsonString)

        Log.d("SocketManager", "emitUserTyping: $userTypingParam")

        socket?.emit("user-typing", jsonObject)
    }

    fun emitUserStopTyping(userTypingParam: UserTypingParam) {
        // Convert MessageModel to JSON string using Gson
        val jsonString = Gson().toJson(userTypingParam)

        // Parse JSON string into JSONObject
        val jsonObject = JSONObject(jsonString)

        Log.d("SocketManager", "emitUserTyping: $userTypingParam")

        socket?.emit("user-stop-typing", jsonObject)
    }

    fun onUserTyping(action: (userTypingParam: UserTypingParam) -> Unit) {
        socket?.on("update-user-typing") {
            val data = it[0] as JSONObject

            val userTypingParam = Gson().fromJson(data.toString(), UserTypingParam::class.java)

            Log.d("SocketManager", "onUserTyping: $userTypingParam")

            action(userTypingParam)
        }
    }

    fun onUserStopTyping(action: (userTypingParam: UserTypingParam) -> Unit) {
        socket?.on("update-user-stop-typing") {
            val data = it[0] as JSONObject

            val userTypingParam = Gson().fromJson(data.toString(), UserTypingParam::class.java)

            Log.d("SocketManager", "onUserStopTyping: $userTypingParam")

            action(userTypingParam)
        }
    }

    fun onDisconnect(action: () -> Unit) {
        socket?.on(Socket.EVENT_DISCONNECT) {
            action()
        }
    }
}