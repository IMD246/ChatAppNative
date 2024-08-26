package com.example.chatappnative.data.socket

import android.util.Log
import com.example.chatappnative.core.constants.NetworkUrl
import com.example.chatappnative.data.local_database.Preferences
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URI
import java.net.URISyntaxException

class SocketManager(
    private val preferences: Preferences
) {
    var socket: Socket? = null

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

    fun onUserPresence(action: (JSONObject) -> Unit) {
        socket?.on("updateUserPresence") {
            Log.d("SocketManager", "onUserPresence: ")
            val data = it[0] as JSONObject

            action(data)
        }
    }

    fun disconnect() {
        socket?.disconnect()
    }
}