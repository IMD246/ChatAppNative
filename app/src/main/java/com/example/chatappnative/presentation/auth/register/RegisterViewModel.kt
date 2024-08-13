package com.example.chatappnative.presentation.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.ResponseState
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val _nameController = MutableStateFlow("")
    var nameController = _nameController.asStateFlow()
    private var _errorName = ""

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


    fun onChangedNameController(value: String) {
        _nameController.value = value
        onValidationName(value)
        canRegister()
    }

    fun onValidationName(value: String) {
        _errorName = ValidatorUtil.validateName(value)
    }

    fun onChangedPasswordController(value: String) {
        _passwordController.value = value
        onValidationPassword(value)
        canRegister()
    }

    fun onValidationPassword(value: String) {
        _errorPassword = ValidatorUtil.validatePassword(value)
    }

    fun onChangedEmailController(value: String) {
        _emailController.value = value
        onValidationEmail(value)
        canRegister()
    }

    fun onValidationEmail(value: String) {
        _errorEmail = ValidatorUtil.validateEmail(value)
    }

    private fun canRegister(): Boolean {
        onValidationEmail(_emailController.value)
        onValidationName(_nameController.value)
        onValidationPassword(_passwordController.value)

        if (_errorEmail.isNotEmpty()) {
            _enabled.value = false;
            return false;
        }

        if (_errorPassword.isNotEmpty()) {
            _enabled.value = false;
            return false;
        }

        if (_errorName.isNotEmpty()) {
            _enabled.value = false;
            return false;
        }

        _enabled.value = true;
        return true;
    }

    fun onRegister() {
        if (!canRegister()) {
            _showError.value = true;
            return;
        }
        _showError.value = false;

        viewModelScope.launch {
            authRepository.register(
                _nameController.value,
                _emailController.value,
                _passwordController.value,
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        dialogAPIHelper.showDialog(it)
                        delay(2000)
                        dialogAPIHelper.hideDialog()
                        Log.d("Register", "onRegister: Error ${it.message}")
                    }

                    is ResponseState.Loading -> {
                        dialogAPIHelper.showDialog(stateAPI = it)
                    }

                    is ResponseState.Success -> {
                        dialogAPIHelper.showDialog(it)
                        delay(2000)
                        dialogAPIHelper.hideDialog()
                        Log.d("Register", "onRegister: Success ${it.message}")
                    }
                }
            }
        }
    }
}