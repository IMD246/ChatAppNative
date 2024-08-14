package com.example.chatappnative.presentation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BaseButton(
    modifier: Modifier? = null,
    enabled: Boolean = true,
    title: String,
    titleColorEnabled: Color? = null,
    bgColorEnabled: Color? = null,
    titleColorDisabled: Color? = null,
    bgColorDisabled: Color? = null,
    shapeRadius: Dp = 30.dp,
    titleStyle: TextStyle? = null,
    onClick: () -> Unit,
) {

    val titleColor: Color
    val bgButton: Color

    if (enabled) {
        titleColor = titleColorEnabled ?: Color.White
        bgButton = bgColorEnabled ?: MaterialTheme.colorScheme.primary
    } else {
        titleColor = titleColorDisabled ?: Color.White.copy(alpha = 0.5F)
        bgButton = bgColorDisabled ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
    }

    val modifier = modifier ?: Modifier
        .fillMaxWidth()
        .padding(horizontal = 30.dp)

    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(shapeRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgButton,
            contentColor = titleColor,
            disabledContentColor = titleColor,
            disabledContainerColor = bgButton,
        )
    ) {
        Text(
            text = title,
            style = titleStyle ?: TextStyle(
                color = titleColor,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            ),
        )
    }
}
