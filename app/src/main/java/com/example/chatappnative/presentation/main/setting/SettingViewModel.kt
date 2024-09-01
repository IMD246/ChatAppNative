package com.example.chatappnative.presentation.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
@Inject constructor(
    private val socketManager: SocketManager,
    private val authRepository: AuthRepository,
    private val preferences: Preferences,
) : ViewModel() {
    private val navigateChannel = Channel<NavigateSettingScreen>()
    val navigateChannelFlow = navigateChannel.receiveAsFlow()

    fun onLogout() {
        viewModelScope.launch {
            authRepository.logout().collect {
                preferences.logout()
                socketManager.disconnect()
                navigateChannel.send(NavigateSettingScreen.Main)
            }
        }
    }
}

sealed class NavigateSettingScreen {
    data object Main : NavigateSettingScreen()
}