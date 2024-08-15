package com.example.chatappnative.presentation.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.domain.repository.AuthRepository
import com.example.chatappnative.helper.DialogAPIHelper
import com.example.chatappnative.util.ValidatorUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferences: Preferences
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val _success = MutableStateFlow(false)
    var success = _success.asStateFlow()

    private val _passwordController = MutableStateFlow("")
    var passwordController = _passwordController.asStateFlow()
    private var _errorPassword = ""

    private val _emailController = MutableStateFlow("")
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
            authRepository.login(
                _emailController.value,
                _passwordController.value,
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        dialogAPIHelper.hideDialog()
                        dialogAPIHelper.showDialog(it)
                        delay(2000)
                        dialogAPIHelper.hideDialog()
                        _success.value = false
                        Log.d("Login", "onLogin: Error ${it.message}")
                    }

                    is ResponseState.Loading -> {
                        _success.value = false
                        dialogAPIHelper.showDialog(stateAPI = it)
                    }

                    is ResponseState.Success -> {
                        dialogAPIHelper.hideDialog()
                        dialogAPIHelper.showDialog(it)
                        preferences.saveAccessToken(it.data?.accessToken ?: "")
                        delay(2000)
                        dialogAPIHelper.hideDialog()
                        _success.value = true
                        Log.d("Login", "onLogin: Success ${it.message}")
                    }
                }
            }
        }
    }
}