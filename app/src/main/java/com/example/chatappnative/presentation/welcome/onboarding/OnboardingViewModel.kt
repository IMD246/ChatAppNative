package com.example.chatappnative.presentation.welcome.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.local_database.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: Preferences
) : ViewModel() {
    private val currentPage = MutableStateFlow(0)
    private val navigateChannel = Channel<NavigateOnboardingScreen>()
    val navigateChannelFlow = navigateChannel.receiveAsFlow()

    @Composable
    fun getCurrentPage(): Int {
        return currentPage.collectAsState().value
    }

    fun onSkipOrGetStarted() {
        viewModelScope.launch {
            preferences.saveOnboarding()
            navigateChannel.send(NavigateOnboardingScreen.Login)
        }
    }

    fun onNext() {
        currentPage.value += 1
    }
}

sealed class NavigateOnboardingScreen {
    data object Login : NavigateOnboardingScreen()
}