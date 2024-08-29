package com.example.chatappnative.presentation.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.AuthRepository
import com.example.chatappnative.helper.DialogAPIHelper
import com.example.chatappnative.util.ValidatorUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferences: Preferences,
    private val socketManager: SocketManager
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val _success = MutableStateFlow(false)
    var success = _success.asStateFlow()

    private val _passwordController = MutableStateFlow("123456")
    var passwordController = _passwordController.asStateFlow()
    private var _errorPassword = ""

    private val _emailController = MutableStateFlow("devnguyen123456@gmail.com")
    var emailController = _emailController.asStateFlow()
    private var _errorEmail = ""

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
            val token = Firebase.messaging.token.await()
            authRepository.login(
                _emailController.value,
                _passwordController.value,
                token
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        dialogAPIHelper.hideDialog()
                        dialogAPIHelper.showDialog(it)
                        _success.value = false
                        Log.d("Login", "onLogin: Error ${it.message}")
                    }

                    is ResponseState.Loading -> {
                        _success.value = false
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
                                _success.value = true
                            }
                        }

                        Log.d("Login", "onLogin: Success ${it.message}")
                    }
                }
            }
        }
    }
}