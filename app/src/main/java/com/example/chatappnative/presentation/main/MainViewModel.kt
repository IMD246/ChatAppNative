package com.example.chatappnative.presentation.main

import androidx.lifecycle.ViewModel
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val socketManager: SocketManager,
    private val preferences: Preferences,
) : ViewModel() {
    private val _isSettingScreen = MutableStateFlow(false)
    val isSettingScreen = _isSettingScreen

    fun init() {
        socketManager.emitLoggedInEvent()
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