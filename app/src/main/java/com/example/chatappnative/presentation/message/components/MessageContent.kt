package com.example.chatappnative.presentation.message.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.R
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.presentation.composables.BaseListReverse
import com.example.chatappnative.presentation.composables.GifImage
import com.example.chatappnative.presentation.composables.NetworkImage
import com.example.chatappnative.presentation.message.MessageViewModel
import com.example.chatappnative.ui.theme.Color191919

@Composable
fun MessageContent(messageViewModel: MessageViewModel) {
    val messageList = messageViewModel.messageList.collectAsState().value.asReversed()

    BaseListReverse(
        items = messageList,
        contentItem = {
            MessageItem(item = it)
        },
        emptyContent = {
            Text(text = "No message")
        },
        loadingContent = {},
        loadMoreContent = {},
        onLoadMore = {

        },
        keyItem = { it.id },
        verticalArrangement = Arrangement.Bottom,
        autoScrollToBottom = true,
    )
}

@Composable
private fun MessageItem(item: MessageModel) {
    val horizontalAlignment: Arrangement.Horizontal =
        if (item.isMine) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalAlignment,
    ) {
        if (!item.isMine) {
            NetworkImage(
                url = "https://fps.cdnpk.net/images/home/subhome-ai.webp?w=649&h=649",
                size = 26.dp,
                modifier = Modifier.align(alignment = Alignment.CenterVertically),
            )
        }
        if (!item.isMine) {
            Spacer(modifier = Modifier.width(10.dp))
        }
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = if (item.isMine) Alignment.TopEnd else Alignment.TopStart
        ) {
            val maxWidth = this.maxWidth

            when (item.type) {
                "text" -> TextMessage(maxWidth, item)
                "image" -> Box {}
            }
        }
    }
}


@Composable
private fun TextMessage(maxWidth: Dp, item: MessageModel) {
    val content = @Composable {
        if (item.status == "typing") {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                GifImage(
                    data = R.drawable.anim_typing_message, modifier = Modifier.size(24.dp),
                )
            }
        } else {
            Column(
                modifier = Modifier.sizeIn(minWidth = 70.dp, maxWidth = maxWidth * 0.7F),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = item.message,
                    modifier = Modifier.align(alignment = Alignment.Start),
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color191919.copy(alpha = 0.95F),
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ) {
                    Box(modifier = Modifier.align(alignment = Alignment.Bottom)) {
                        TimeStamp()
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.align(alignment = Alignment.Bottom)) {
                        StatusMessage(item.status)
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .background(
                color = Color(0xFFB5E2E2),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(10.dp),
    )
    {
        content()
    }
}

@Composable
private fun TimeStamp() {
    Text(
        text = "6:05",
        style = TextStyle(
            fontSize = 8.sp,
            color = Color191919.copy(alpha = 0.6F),
        ),
    )
}

@Composable
private fun StatusMessage(type: String) {
    val statusComposable = @Composable {
        when (type) {
            "not-sent" -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_sent_message),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color191919.copy(alpha = 0.25F)),
                )
            }

            "sent" -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_sent_message),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color191919.copy(alpha = 0.75F))
                )
            }

            "read" -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_read_message),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color191919.copy(alpha = 0.75F))
                )
            }
        }
    }

    statusComposable()
}