package com.example.chatappnative.presentation.composables

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner.current
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BackButton() {
    val onBackPressedDispatcher = current?.onBackPressedDispatcher

    IconButton(
        modifier = Modifier.size(22.dp),
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