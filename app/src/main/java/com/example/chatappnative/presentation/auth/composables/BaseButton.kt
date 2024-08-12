package com.example.chatappnative.presentation.auth.composables

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BaseButton(
    enabled: Boolean = true,
    title: String,
    onClick: () -> Unit,
) {

    val titleColor: Color
    val bgButton: Color

    if (enabled) {
        titleColor = Color.White
        bgButton = MaterialTheme.colorScheme.primary
    } else {
        titleColor = Color.White.copy(alpha = 0.5F)
        bgButton = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
    }
    
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        onClick = onClick,
        shape = RoundedCornerShape(30.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgButton,
            contentColor = titleColor,
            disabledContentColor = titleColor,
            disabledContainerColor = bgButton,
        )
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = titleColor,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            ),
        )
    }
}
