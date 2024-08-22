package com.example.chatappnative.presentation.composables

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner.current
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@JvmOverloads
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopSection(
    useBackNavigation: Boolean = true,
    title: String,
    subTitle: String? = null,
) {
    val onBackPressedDispatcher = current?.onBackPressedDispatcher

    val navigationIcon: @Composable () -> Unit = {
        if (useBackNavigation) BackButton()
    }

    LargeTopAppBar(
        modifier = Modifier.clip(shape = RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White,
        ),
        navigationIcon = navigationIcon,
        title = {
            Column(modifier = Modifier.defaultMinSize()) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 30.sp,
                )
                if (subTitle != null) {
                    Text(
                        text = subTitle,
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                    )
                }
            }
        },
    )
}