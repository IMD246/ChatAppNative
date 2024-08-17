package com.example.chatappnative.data.socket

import android.util.Log
import com.example.chatappnative.core.constants.NetworkUrl
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URI
import java.net.URISyntaxException


class SocketManager {
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


    fun disconnect() {
        socket?.disconnect()
    }
}