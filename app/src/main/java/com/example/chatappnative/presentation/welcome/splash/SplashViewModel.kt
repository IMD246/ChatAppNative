package com.example.chatappnative.presentation.welcome.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.local_database.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {
    private val navigateChannel = Channel<NavigateSplashScreen>()
    val navigateChannelFlow = navigateChannel.receiveAsFlow()

    init {
        onNavigate()
    }

    private fun onNavigate() {
        val isOnboarding = preferences.getOnboarding()
        val accessToken = preferences.getAccessToken()
        val userInfo = preferences.getUserInfo()
        viewModelScope.launch {
            if (isOnboarding) {
                if (accessToken.isEmpty() || accessToken.isBlank() || userInfo == null) {
                    navigateChannel.send(NavigateSplashScreen.Login)
                } else {
                    if (userInfo.isExpired()) {
                        preferences.logout()
                        navigateChannel.send(NavigateSplashScreen.Login)
                        return@launch
                    }

                    navigateChannel.send(NavigateSplashScreen.Main)
                }
            } else {
                navigateChannel.send(NavigateSplashScreen.Onboarding)
            }
        }
    }
}

sealed class NavigateSplashScreen {
    data object Onboarding : NavigateSplashScreen()
    data object Login : NavigateSplashScreen()
    data object Main : NavigateSplashScreen()
}