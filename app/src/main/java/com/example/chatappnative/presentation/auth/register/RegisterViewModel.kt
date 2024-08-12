package com.example.chatappnative.presentation.auth.login

import androidx.lifecycle.ViewModel
import com.example.chatappnative.Util.ValidatorUtil
import com.example.chatappnative.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val authRepository: AuthRepository
) : ViewModel() {
    private val _nameController = MutableStateFlow("")
    var nameController = _nameController.asStateFlow()
    private var _errorName = ""

    private val _passwordController = MutableStateFlow("")
    var passwordController = _passwordController.asStateFlow()
    private var _errorPassword = ""

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

    fun onChangedPhoneController(value: String) {
        _phoneController.value = value
        onValidationPhone(value)
        canRegister()
    }

    fun onValidationPhone(value: String) {
        _errorPhone = ValidatorUtil.validatePhone(value)
    }

    private fun canRegister(): Boolean {
        onValidationPhone(_phoneController.value)
        onValidationName(_nameController.value)
        onValidationPassword(_passwordController.value)

        if (_errorPhone.isNotEmpty()) {
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
    }
}