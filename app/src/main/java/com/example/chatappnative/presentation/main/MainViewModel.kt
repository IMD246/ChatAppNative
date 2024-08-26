package com.example.chatappnative.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chatappnative.data.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val socketManager: SocketManager,
) : ViewModel() {

    fun init() {
        Log.d("MainActivity", "Call emit logged in event")
        socketManager.emitLoggedInEvent()
    }
}