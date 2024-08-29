package com.example.chatappnative.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.service.ConnectivityInternetObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val socketManager: SocketManager,
    private val preferences: Preferences,
    private val connectivityInternetObserver: ConnectivityInternetObserver
) : ViewModel() {
    private val _isSettingScreen = MutableStateFlow(false)
    val isSettingScreen = _isSettingScreen

    init {
        socketManager.emitLoggedInEvent()
        viewModelScope.launch {
            handleConnectivity()
        }
    }

    private suspend fun handleConnectivity() {
        connectivityInternetObserver.isConnected.collectLatest { connected ->
            Log.d("MainViewModel", "handleConnectivity: $connected")
            if (!connected) return@collectLatest
            if (socketManager.socket?.connected() == true) return@collectLatest

            socketManager.socket?.connect()
            socketManager.updateNeedReconnect(false)
            Log.d("MainViewModel", "handleConnectivity: called connect")
            socketManager.onConnect {
                socketManager.emitReconnect()
            }
        }
    }

    fun getActivityPending(): String {
        return preferences.getActivityPending()
    }

    fun clearActivityPending(): Unit {
        preferences.saveActivityPending("")
    }

    fun updateIsSettingScreen(isSettingScreen: Boolean) {
        _isSettingScreen.value = isSettingScreen
    }
}