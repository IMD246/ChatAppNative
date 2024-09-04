package com.example.chatappnative.presentation.message.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.R
import com.example.chatappnative.presentation.composables.BackButton
import com.example.chatappnative.presentation.composables.NetworkImage
import com.example.chatappnative.presentation.message.MessageViewModel
import com.example.chatappnative.util.DateFormatUtil
import kotlinx.coroutines.delay

@Composable
fun AppBarMessage(messageModel: MessageViewModel) {
    val presence = false
    val date = DateFormatUtil.getCurrentLocalDate()

    val presenceValue = if (presence) {
        "Đang hoạt động"
    } else {
        DateFormatUtil.presenceMessageFormat(date)
    }

    var presenceText by remember {
        mutableStateOf(presenceValue)
    }

    LaunchedEffect(Unit) {
        while (true) {
            if (DateFormatUtil.isDiffSecondsMoreThanADay(date)) break;

            presenceText = DateFormatUtil.presenceMessageFormat(date)

            delay(DateFormatUtil.getDelayDiffMilliseconds(date))
        }
    }

    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier.clip(
            RoundedCornerShape(
                bottomEnd = 25.dp,
                bottomStart = 25.dp
            )
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                BackButton()
                Spacer(modifier = Modifier.width(6.dp))
                Box {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        NetworkImage(
                            url = "https://www.simplilearn.com/ice9/free_resources_article_thumb/what_is_image_Processing.jpg",
                            size = 30.dp,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(start = 20.dp, end = 0.dp, top = 12.dp)
                    ) {
                        Presence()
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Test1", style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        presenceText,
                        style = TextStyle(
                            color = Color.White.copy(0.7F),
                            fontSize = 10.sp,
                        )
                    )
                }
            }
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .size(16.dp),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_call),
                    contentDescription = "call",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                modifier = Modifier
                    .size(16.dp), onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_video),
                    contentDescription = "video",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(
                modifier = Modifier
                    .size(16.dp),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more_options),
                    contentDescription = "more_options",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    )
}

@Composable
private fun Presence() {
    val presence = false

    val presenceComposable: @Composable () -> Unit = {
        if (presence) {
            Box(
                Modifier
                    .size(10.dp)
                    .background(
                        color = Color(0xFF48D357),
                        shape = CircleShape,
                    )
            )
        } else {
            Box(modifier = Modifier.size(0.dp))
        }
    }

    presenceComposable()
}