@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.chatappnative.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.ui.theme.ColorF9FFFF


@Composable
fun BaseSearchBar(
    modifier: Modifier? = null,
    value: String = "",
    hint: String,
    focusRequester: FocusRequester? = null,
    isShowError: Boolean = false,
    maxLengths: Int? = null,
    onSubmitted: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val inputData = remember { mutableStateOf(value) }

    val visibleClearIcon: @Composable () -> Unit = if (inputData.value.isNotEmpty()) {
        {
            Icon(
                imageVector = Icons.Default.Clear, contentDescription = "Search",
                tint = Color191919.copy(alpha = 0.75F)
            )
        }
    } else {
        {}
    }

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

    val borderColor = if (isFocused) {
        Color.White
    } else {
        Color191919.copy(alpha = 0.1F)
    }

    dataModifier.focusRequester(focus)

    OutlinedTextField(
        modifier = dataModifier
            .shadow(
                1.dp, shape = RoundedCornerShape(25.dp)
            ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSubmitted(inputData.value)
                focusManager.clearFocus(true)
            }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        shape = RoundedCornerShape(25.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            unfocusedContainerColor = ColorF9FFFF,
            focusedContainerColor = ColorF9FFFF,
        ),
        value = inputData.value,
        onValueChange = {
            inputData.value = it
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
                    color = Color191919.copy(alpha = 0.4F),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
        },
        leadingIcon = {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "J",
                    style = TextStyle(
                        color = Color191919,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                )
            }
        },
        trailingIcon = {
            Row(modifier = Modifier.padding(horizontal = 10.dp)) {
                IconButton(modifier = Modifier.size(24.dp), onClick = {
                    if (inputData.value.isNotEmpty()) {
                        inputData.value = ""
                    }
                }) {
                    visibleClearIcon()
                }
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search",
                    tint = Color191919.copy(alpha = 0.75F)
                )
            }
        },
    )
}


