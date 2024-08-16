package com.example.chatappnative.presentation.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.presentation.composables.BaseSearchBar

@Composable
fun ChatScreen(chatModel: ChatViewModel) {
    val selectedTabbarIndex = 0

    Box(modifier = Modifier.padding(0.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Column {
                    Spacer(modifier = Modifier.height(35.dp))
                    BaseSearchBar(hint = "Search by name, number...")
                    Spacer(modifier = Modifier.height(35.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.weight(1F))
                        Text(
                            text = "Chats",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = (if (selectedTabbarIndex == 0) {
                                    Color.White
                                } else {
                                    Color.White.copy(alpha = 0.75F)
                                })
                            ),
                        )
                        Spacer(modifier = Modifier.weight(3F))
                        Text(
                            text = "Calls",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = (if (selectedTabbarIndex == 1) {
                                    Color.White
                                } else {
                                    Color.White.copy(alpha = 0.75F)
                                })
                            ),
                        )
                        Spacer(modifier = Modifier.weight(1F))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

        }
    }
}