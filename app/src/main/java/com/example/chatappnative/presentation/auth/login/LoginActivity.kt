package com.example.chatappnative.presentation.auth.login

import android.content.Intent
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.presentation.auth.register.RegisterActivity
import com.example.chatappnative.presentation.composables.BaseButton
import com.example.chatappnative.presentation.composables.BaseInput
import com.example.chatappnative.presentation.composables.ButtonWithoutOuterPadding
import com.example.chatappnative.presentation.composables.LargeTopSection
import com.example.chatappnative.presentation.composables.PasswordInput
import com.example.chatappnative.presentation.main.MainActivity
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.util.ValidatorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatAppNativeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        LargeTopSection(
                            useBackNavigation = false,
                            title = "Login",
                            subTitle = "Fill up your details to login."
                        )
                    }
                ) { innerPadding ->
                    LoginScreen(innerPadding = innerPadding)
                }
            }
        }
    }

    @Composable
    fun LoginScreen(
        innerPadding: PaddingValues,
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        val interactionSource = remember { MutableInteractionSource() }

        // show dialog
        loginModel.dialogAPIHelper.DisplayDialog()

        val isSuccess = loginModel.success.collectAsState().value

        LaunchedEffect(key1 = isSuccess) {
            if (isSuccess) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus(true)
                },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                BaseInput(
                    value = loginModel.emailController.collectAsState().value,
                    onValueChange = {
                        loginModel.onChangedEmailController(it)
                    },
                    hint = "Enter your email",
                    label = "Email",
                    isShowError = loginModel.showError.collectAsState().value,
                    onValidation = {
                        ValidatorUtil.validateEmail(loginModel.emailController.collectAsState().value)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordInput(
                    value = loginModel.passwordController.collectAsState().value,
                    onValueChange = {
                        loginModel.onChangedPasswordController(it)
                    },
                    isShowError = loginModel.showError.collectAsState().value,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus(true)
                            loginModel.onLogin()
                        }
                    ),
                )
                Spacer(modifier = Modifier.height(10.dp))
                BaseButton(
                    enabled = loginModel.enabled.collectAsState().value,
                    title = "Login",
                ) {
                    loginModel.onLogin()
                }
                Spacer(Modifier.weight(1f))
                Row {
                    Text(
                        text = "Don't have an account! ",
                        style = TextStyle(
                            color = Color191919,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        ),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    ButtonWithoutOuterPadding(
                        onClick = {
                            startActivity(Intent(baseContext, RegisterActivity::class.java))
                        }) {
                        Text(
                            text = "Register", style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        )
                    }
                }
            }
        }
    }
}

