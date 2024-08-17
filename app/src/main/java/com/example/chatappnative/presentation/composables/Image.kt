package com.example.chatappnative.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    val dataModifier = modifier
        .size(size)
        .clip(CircleShape)                       // clip to the circle shape

    Image(
        painter = rememberAsyncImagePainter(url),
        contentDescription = null,
        modifier = dataModifier,
        contentScale = ContentScale.Crop,
    )
}