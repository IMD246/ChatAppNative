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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.chatappnative.ui.theme.ColorPrimary
import com.example.chatappnative.util.DateFormatUtil
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarMessage(messageModel: MessageViewModel) {
    val chatDetail = messageModel.chatDetail.collectAsState().value

    val presence = chatDetail?.getPresence() ?: false

    var presenceText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(chatDetail) {
        if (chatDetail == null) return@LaunchedEffect

        while (true) {
            if (!presence) {
                if (DateFormatUtil.isDiffSecondsMoreThanADay(chatDetail.getDateTimePresence())) break

                presenceText =
                    DateFormatUtil.presenceMessageFormat(chatDetail.getDateTimePresence())

                delay(DateFormatUtil.getDelayDiffMilliseconds(chatDetail.getDateTimePresence()))
            } else {
                presenceText = "Đang hoạt động"
                break
            }
        }
    }

    TopAppBar(
        colors = TopAppBarColors(
            containerColor = ColorPrimary,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White,
            titleContentColor = Color.White,
            scrolledContainerColor = Color(0xFF252525),
        ),
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
                            url = chatDetail?.urlImage ?: "",
                            size = 30.dp,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(start = 20.dp, end = 0.dp, top = 12.dp)
                    ) {
                        Presence(presence = presence)
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = chatDetail?.nameChat ?: "", style = TextStyle(
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
                    .size(20.dp),
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
                    .size(20.dp), onClick = {}
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
private fun Presence(presence: Boolean = false) {
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