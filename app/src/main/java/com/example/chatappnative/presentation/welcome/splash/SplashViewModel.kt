package com.example.chatappnative.presentation.welcome.splash

import androidx.lifecycle.ViewModel
import com.example.chatappnative.data.local_database.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    preferences: Preferences
) : ViewModel() {
    private val _isOnboarding = MutableStateFlow(false)
    private val _accessToken = MutableStateFlow("")

    init {
        _isOnboarding.value = preferences.getOnboarding()
        _accessToken.value = preferences.getAccessToken()
    }

    fun getIsOnboarding(): MutableStateFlow<Boolean> {
        return _isOnboarding
    }

    fun getAccessToken(): MutableStateFlow<String> {
        return _accessToken
    }
}