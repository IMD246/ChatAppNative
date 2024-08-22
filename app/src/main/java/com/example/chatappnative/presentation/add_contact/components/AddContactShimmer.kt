package com.example.chatappnative.presentation.add_contact.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatappnative.presentation.composables.shimmerEffect

@Composable
fun AddContactListShimmer() {
    Column(modifier = Modifier.padding(20.dp, 15.dp)) {
        repeat(10) {
            AddContactShimmerItem()
        }
    }
}

@Composable
fun AddContactShimmerItem() {
    Row(
        modifier = Modifier.padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth(0.5F)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.weight(1F))
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(48.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
    }
}