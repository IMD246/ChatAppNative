package com.example.chatappnative.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.presentation.add_contact.AddContactActivity
import com.example.chatappnative.service.ConnectivityInternetObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val socketManager: SocketManager,
    private val preferences: Preferences,
    private val connectivityInternetObserver: ConnectivityInternetObserver
) : ViewModel() {
    private val navigateChannel = Channel<NavigateMainScreen>()
    val navigateChannelFlow = navigateChannel.receiveAsFlow()

    private val _isSettingScreen = MutableStateFlow(false)
    val isSettingScreen = _isSettingScreen

    init {
        checkSocketInitFirstTime()
        handleActivityPending()
        viewModelScope.launch {
            handleConnectivity()
        }
    }

    private fun checkSocketInitFirstTime() {
        if (socketManager.socket?.connected() == true) {
            socketManager.emitLoggedInEvent()
        }
    }

    private suspend fun handleConnectivity() {
        connectivityInternetObserver.isConnected.collectLatest { connected ->
            Log.d("MainViewModel", "handleConnectivity: $connected")
            if (!connected) return@collectLatest

            if (socketManager.socket == null) {
                socketManager.connect()
                socketManager.onConnect {
                    socketManager.emitLoggedInEvent()
                }
                return@collectLatest
            }

            if (socketManager.socket?.connected() == true) return@collectLatest

            socketManager.socket?.connect()
            socketManager.updateNeedReconnect(false)
            Log.d("MainViewModel", "handleConnectivity: called connect")
            socketManager.onConnect {
                socketManager.emitReconnect()
            }
        }
    }

    private fun handleActivityPending(): Unit {
        val activityPending = preferences.getActivityPending()

        if (activityPending.isEmpty()) return

        when (activityPending) {
            AddContactActivity::class.java.name -> {
                onNavigateChanged(NavigateMainScreen.ADDCONTACT)
                clearActivityPending()
            }

            MainActivity::class.java.name -> {
                onNavigateChanged(NavigateMainScreen.CONTACT)
                clearActivityPending()
            }
        }
    }

    private fun clearActivityPending() {
        preferences.saveActivityPending("")
    }

    fun updateIsSettingScreen(isSettingScreen: Boolean) {
        _isSettingScreen.value = isSettingScreen
    }

    fun onNavigateChanged(navigateMainScreen: NavigateMainScreen) {
        viewModelScope.launch {
            navigateChannel.send(navigateMainScreen)
        }
    }

    fun logout() {
        preferences.logout()
    }
}

sealed class NavigateMainScreen {
    data object CHAT : NavigateMainScreen()
    data object CONTACT : NavigateMainScreen()
    data object SETTING : NavigateMainScreen()
    data object LOGIN : NavigateMainScreen()
    data object ADDCONTACT : NavigateMainScreen()
}