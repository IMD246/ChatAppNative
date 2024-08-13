@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatappnative.presentation.auth.composables

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chatappnative.R
import com.example.chatappnative.util.ValidatorUtil


@Composable
fun PasswordInput(
    value: String = "",
    onValueChange: (String) -> Unit,
    isShowError: Boolean,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val visiblePassword = remember {
        mutableStateOf(false)
    }

    val visibleIcon: @Composable () -> Unit = if (visiblePassword.value) {
        {
            Icon(
                painter = painterResource(id = R.drawable.ic_visibility_off),
                contentDescription = "Localized description",
            )
        }
    } else {
        {
            Icon(
                painter = painterResource(id = R.drawable.ic_visibility),
                contentDescription = "Localized description",
            )
        }
    }

    BaseInput(
        hint = "Enter your password",
        label = "Password",
        onValueChange = onValueChange,
        onValidation = {
            ValidatorUtil.validatePassword(value)
        },
        suffix = {
            IconButton(modifier = Modifier.size(24.dp), onClick = {
                visiblePassword.value = !visiblePassword.value
            }) {
                visibleIcon()
            }
        },
        value = value,
        isShowError = isShowError,
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
        ),
        visualTransformation = if (visiblePassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}


