package com.example.chatappnative.presentation.auth.register

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.AuthRepository
import com.example.chatappnative.helper.DialogAPIHelper
import com.example.chatappnative.util.ValidatorUtil
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val socketManager: SocketManager,
    private val preferences: Preferences,
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val _success = MutableStateFlow(false)
    var success = _success.asStateFlow()

    private val _nameController = MutableStateFlow("")
    var nameController = _nameController.asStateFlow()
    private var _errorName = ""

    private val _passwordController = MutableStateFlow("")
    var passwordController = _passwordController.asStateFlow()
    private var _errorPassword = ""

    private val _emailController = MutableStateFlow("")
    var emailController = _emailController.asStateFlow()
    private var _errorEmail = ""

    private val _phoneController = MutableStateFlow("")
    var phoneController = _phoneController.asStateFlow()
    private var _errorPhone = ""

    private val _showError = MutableStateFlow(false)
    var showError = _showError.asStateFlow()

    private val _enabled = MutableStateFlow(false)
    var enabled = _enabled.asStateFlow()


    fun onChangedNameController(value: String) {
        _nameController.value = value
        onValidationName(value)
        canRegister()
    }

    private fun onValidationName(value: String) {
        _errorName = ValidatorUtil.validateName(value)
    }

    fun onChangedPasswordController(value: String) {
        _passwordController.value = value
        onValidationPassword(value)
        canRegister()
    }

    private fun onValidationPassword(value: String) {
        _errorPassword = ValidatorUtil.validatePassword(value)
    }

    fun onChangedEmailController(value: String) {
        _emailController.value = value
        onValidationEmail(value)
        canRegister()
    }

    private fun onValidationEmail(value: String) {
        _errorEmail = ValidatorUtil.validateEmail(value)
    }

    fun onChangedPhoneController(value: String) {
        if (!value.isDigitsOnly()) return
        _phoneController.value = value
        onValidationPhone(value)
        canRegister()
    }

    private fun onValidationPhone(value: String) {
        _errorPhone = ValidatorUtil.validatePhone(value)
    }

    private fun canRegister(): Boolean {
        onValidationEmail(_emailController.value)
        onValidationName(_nameController.value)
        onValidationPassword(_passwordController.value)
        onValidationPhone(_phoneController.value)

        if (_errorEmail.isNotEmpty()) {
            _enabled.value = false
            return false
        }

        if (_errorPassword.isNotEmpty()) {
            _enabled.value = false
            return false
        }

        if (_errorName.isNotEmpty()) {
            _enabled.value = false
            return false
        }

        if (_errorPhone.isNotEmpty()) {
            _enabled.value = false
            return false
        }

        _enabled.value = true
        return true
    }

    fun onRegister() {
        if (!canRegister()) {
            _showError.value = true
            return
        }
        _showError.value = false

        viewModelScope.launch {
            authRepository.register(
                _nameController.value,
                _emailController.value,
                _phoneController.value,
                _passwordController.value,
                Firebase.messaging.token.await()
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        dialogAPIHelper.showDialog(it)
                        delay(2000)
                        dialogAPIHelper.hideDialog()
                        _success.value = false
                        Log.d("Register", "onRegister: Error ${it.message}")
                    }

                    is ResponseState.Loading -> {
                        _success.value = false
                        dialogAPIHelper.showDialog(stateAPI = it)
                    }

                    is ResponseState.Success -> {
                        val state = it
                        preferences.saveAccessToken(it.data?.accessToken ?: "")
                        socketManager.connect()
                        socketManager.onConnect {
                            dialogAPIHelper.hideDialog()
                            dialogAPIHelper.showDialog(state)

                            viewModelScope.launch {
                                delay(2000L)
                                dialogAPIHelper.hideDialog()
                                _success.value = true
                            }
                        }

                        Log.d("Register", "onRegister: Success ${it.message}")
                    }
                }
            }
        }
    }
}