package com.example.chatappnative.presentation.auth.login

import androidx.lifecycle.ViewModel
import com.example.chatappnative.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val authRepository: AuthRepository
) : ViewModel()