@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatappnative.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.ui.theme.Color191919


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseInput(
    modifier: Modifier? = null,
    value: String = "",
    hint: String,
    label: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester? = null,
    onValidation: @Composable (String) -> String,
    isShowError: Boolean = false,
    maxLengths: Int? = null,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    suffix: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {

    var isFocused: Boolean by remember { mutableStateOf(false) }

    val focus = remember {
        focusRequester ?: FocusRequester()
    }

    val dataModifier: Modifier

    if (modifier != null) {
        dataModifier = modifier
    } else {
        dataModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .onFocusChanged {
                isFocused = it.hasFocus
            }
    }

    dataModifier.focusRequester(focus)

    var isError = false

    @Composable
    fun onExecuteValidation() {
        isError = onValidation(value).isNotEmpty()
    }

    onExecuteValidation()

    val errorMode: Boolean = if (isShowError)
        isError
    else
        isError && isFocused

    val colorLabel: Color = if (errorMode) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.primary
    }

    OutlinedTextField(
        modifier = dataModifier,
        shape = RoundedCornerShape(25.dp),
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        isError = errorMode,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            errorLabelColor = MaterialTheme.colorScheme.error,
        ),
        supportingText = {
            if (errorMode) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = onValidation(value),
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        value = value,
        onValueChange = {
            if (it.length > (maxLengths ?: 1000000)) {
                onValueChange(value)
            } else {
                onValueChange(it)
            }
        },
        textStyle = TextStyle(
            color = Color191919,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
        ),
        placeholder = {
            Text(
                hint,
                style = TextStyle(
                    color = Color191919.copy(alpha = 0.5F),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
        },
        label = {
            Text(
                label,
                style = TextStyle(
                    color = colorLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
        },
        suffix = suffix,
        visualTransformation = visualTransformation,
    )
}

