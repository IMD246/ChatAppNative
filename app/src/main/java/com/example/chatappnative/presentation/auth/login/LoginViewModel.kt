package com.example.chatappnative.presentation.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.param.LoginParam
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.AuthRepository
import com.example.chatappnative.helper.DialogAPIHelper
import com.example.chatappnative.util.ValidatorUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferences: Preferences,
    private val socketManager: SocketManager
) : ViewModel() {
    private val navigateChannel = Channel<NavigateLoginScreen>()
    val navigateChannelFlow = navigateChannel.receiveAsFlow()

    val dialogAPIHelper = DialogAPIHelper()

    private val _emailController = MutableStateFlow("duybagai@gmail.com")
    var emailController = _emailController.asStateFlow()
    private var _errorEmail = ""

    private val _passwordController = MutableStateFlow("123456")
    var passwordController = _passwordController.asStateFlow()
    private var _errorPassword = ""

    private val _showError = MutableStateFlow(false)
    var showError = _showError.asStateFlow()

    private val _enabled = MutableStateFlow(false)
    var enabled = _enabled.asStateFlow()


    fun onChangedPasswordController(value: String) {
        _passwordController.value = value
        onValidationPassword(value)
        canLogin()
    }

    private fun onValidationPassword(value: String) {
        _errorPassword = ValidatorUtil.validatePassword(value)
    }

    fun onChangedEmailController(value: String) {
        _emailController.value = value
        onValidationEmail(value)
        canLogin()
    }

    private fun onValidationEmail(value: String) {
        _errorEmail = ValidatorUtil.validateEmail(value)
    }

    private fun canLogin(): Boolean {
        onValidationEmail(_emailController.value)
        onValidationPassword(_passwordController.value)

        if (_errorEmail.isNotEmpty()) {
            _enabled.value = false
            return false
        }

        if (_errorPassword.isNotEmpty()) {
            _enabled.value = false
            return false
        }

        _enabled.value = true
        return true
    }

    fun onLogin() {
        if (!canLogin()) {
            _showError.value = true
            return
        }
        _showError.value = false

        viewModelScope.launch {
            authRepository.login(
                LoginParam(
                    _emailController.value,
                    _passwordController.value,
                    Firebase.messaging.token.await()
                )
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        dialogAPIHelper.hideDialog()
                        dialogAPIHelper.showDialog(it)
                        Log.d("Login", "onLogin: Error ${it.message}")
                    }

                    is ResponseState.Loading -> {
                        dialogAPIHelper.showDialog(stateAPI = it)
                    }

                    is ResponseState.Success -> {
                        val state = it
                        preferences.saveAccessToken(it.data?.accessToken ?: "")
                        preferences.saveUserInfo(it.data!!)

                        socketManager.connect()

                        socketManager.onConnect {
                            dialogAPIHelper.hideDialog()
                            dialogAPIHelper.showDialog(state)

                            viewModelScope.launch {
                                delay(2000L)
                                navigateChannel.send(NavigateLoginScreen.Main)
                            }
                        }

                        Log.d("Login", "onLogin: Success ${it.message}")
                    }
                }
            }
        }
    }

    fun onRegister() {
        viewModelScope.launch {
            navigateChannel.send(NavigateLoginScreen.Register)
        }
    }
}

sealed class NavigateLoginScreen {
    data object Register : NavigateLoginScreen()
    data object Main : NavigateLoginScreen()
}