package com.example.chatappnative.presentation.welcome.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.chatappnative.data.local_database.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: Preferences
) : ViewModel() {
    private val currentPage = MutableStateFlow(0)

    @Composable
    fun getCurrentPage(): Int {
        return currentPage.collectAsState().value
    }

    fun onSkipOrGetStarted() {
        preferences.saveOnboarding()
    }

    fun onNext() {
        currentPage.value += 1
    }
}