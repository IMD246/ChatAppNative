package com.example.chatappnative.presentation.composables

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner.current
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BackButton() {
    val onBackPressedDispatcher = current?.onBackPressedDispatcher

    IconButton(
        onClick = {
            onBackPressedDispatcher?.onBackPressed()
        }, colors = IconButtonDefaults.iconButtonColors(
            contentColor = Color.White,
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back"
        )
    }
}