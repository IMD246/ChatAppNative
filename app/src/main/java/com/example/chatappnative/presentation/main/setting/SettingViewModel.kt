package com.example.chatappnative.presentation.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
@Inject constructor(
    private val socketManager: SocketManager,
    private val authRepository: AuthRepository,
    private val preferences: Preferences,
) : ViewModel() {
    fun onLogout() {
        viewModelScope.launch {
            authRepository.logout().collect {

            }
            socketManager.disconnect()
            preferences.logout()
        }
    }
}