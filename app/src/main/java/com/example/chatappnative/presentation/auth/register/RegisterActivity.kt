package com.example.chatappnative.presentation.auth.register

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.chatappnative.Util.ValidatorUtil
import com.example.chatappnative.presentation.auth.composables.BaseButton
import com.example.chatappnative.presentation.auth.composables.BaseInput
import com.example.chatappnative.presentation.auth.composables.LargeTopSection
import com.example.chatappnative.presentation.auth.login.RegisterViewModel
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {

    private val registeriewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAppNativeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        LargeTopSection(
                            title = "Small Top App Bar",
                            subTitle = "Fill up your details to register."
                        )
                    }
                ) { innerPadding ->
                    RegisterScreen(innerPadding = innerPadding)
                }
            }
        }
    }

    @Composable
    fun RegisterScreen(
        innerPadding: PaddingValues,
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }

        Box(modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus(true)
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                Spacer(modifier = Modifier.height(10.dp))
                BaseInput(
                    value = registeriewModel.nameController.collectAsState().value,
                    onValueChange = {
                        registeriewModel.onChangedNameController(it)
                    },
                    isShowError = registeriewModel.showError.collectAsState().value,
                    hint = "Enter your name",
                    label = "Name",
                    onValidation = {
                        ValidatorUtil.validateName(registeriewModel.nameController.collectAsState().value)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next);
                        }
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                BaseInput(
                    value = registeriewModel.passwordController.collectAsState().value,
                    onValueChange = {
                        registeriewModel.onChangedPasswordController(it)
                    },
                    isShowError = registeriewModel.showError.collectAsState().value,
                    hint = "Enter your password",
                    label = "Password",
                    onValidation = {
                        ValidatorUtil.validatePassword(registeriewModel.passwordController.collectAsState().value)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next);
                        }
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                BaseInput(
                    value = registeriewModel.phoneController.collectAsState().value,
                    onValueChange = {
                        registeriewModel.onChangedPhoneController(it)
                    },
                    hint = "Enter your phone number",
                    label = "Phone",
                    isShowError = registeriewModel.showError.collectAsState().value,
                    onValidation = {
                        ValidatorUtil.validatePhone(registeriewModel.phoneController.collectAsState().value)
                    },
                    maxLengths = 11,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                            registeriewModel.onRegister()
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                BaseButton(
                    enabled = registeriewModel.enabled.collectAsState().value,
                    title = "Register",
                ) {
                    registeriewModel.onRegister()
                }
            }
        }
    }
}

