package com.example.chatappnative.presentation.main

import androidx.lifecycle.ViewModel
import com.example.chatappnative.data.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val socketManager: SocketManager,
) : ViewModel() {

    fun init() {
        socketManager.emitLoggedInEvent()
    }
}