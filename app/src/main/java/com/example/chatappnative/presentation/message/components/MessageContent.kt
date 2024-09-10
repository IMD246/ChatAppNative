package com.example.chatappnative.presentation.message.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.R
import com.example.chatappnative.data.model.ChatDetailModel
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.param.StatusMessage.TYPING
import com.example.chatappnative.data.param.TypeMessage
import com.example.chatappnative.presentation.composables.BaseListReverse
import com.example.chatappnative.presentation.composables.GifImage
import com.example.chatappnative.presentation.composables.NetworkImage
import com.example.chatappnative.presentation.message.MessageViewModel
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.ui.theme.ColorPrimary
import com.example.chatappnative.util.DateFormatUtil
import java.util.Date

@SuppressLint("SimpleDateFormat")
@Composable
fun MessageContent(messageViewModel: MessageViewModel) {
    val groupedByMessages = messageViewModel.groupedByMessages.collectAsState().value
    val chatDetail = messageViewModel.chatDetail.collectAsState().value
    val isLoadingMessageList = messageViewModel.isLoadingMessageList.collectAsState().value
    val isLoadMore = messageViewModel.isMessageListLoadMore.collectAsState().value
    val isTyping = messageViewModel.isTyping.collectAsState().value
    val newMessage = messageViewModel.newMessageFlow.collectAsState().value

    val triggerScroll =
        messageViewModel.triggerScroll.collectAsState().value

    var customScrollToEnd: @Composable (() -> Unit)? = null

    if (newMessage != null) {
        customScrollToEnd = {
            NetworkImage(
                newMessage.senderAvatar,
                size = 32.dp,
            )
        }
    }

    BaseListReverse(
        isGroupByList = true,
        items = groupedByMessages,
        triggerScroll = triggerScroll,
        isLoadMore = isLoadMore,
        isLoading = isLoadingMessageList,
        onScrollToEnd = {
            messageViewModel.clearNewMessage()
        },
        customIconEnableScrollButton = customScrollToEnd,
        onTriggerScroll = {
            messageViewModel.onUpdateTriggerScroll(false)
        },
        contentItem = {
            Column {
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally),
                    text = it.first,
                )
                it.second.forEach { messageModel: MessageModel ->
                    MessageItem(item = messageModel, presence = chatDetail?.getPresence() ?: false)
                }
            }
        },
        emptyContent = {
            EmptyContent(chatDetail = chatDetail)
        },
        loadingContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = ColorPrimary
                )
            }
        },
        loadMoreContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = ColorPrimary
                )
            }
        },
        onLoadMore = {
            messageViewModel.loadMore()
        },
        keyItem = { it.first },
        verticalArrangement = Arrangement.Bottom,
        isTyping = isTyping,
    )
}

@Composable
private fun EmptyContent(chatDetail: ChatDetailModel?) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        NetworkImage(
            chatDetail?.urlImage
                ?: "",
            size = 100.dp,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = chatDetail?.nameChat ?: "", style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color191919.copy(alpha = 0.95F),
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun MessageItem(item: MessageModel, presence: Boolean = false) {
    val horizontalAlignment: Arrangement.Horizontal =
        if (item.isMine) Arrangement.End else Arrangement.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = horizontalAlignment,
    ) {
        Box(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                NetworkImage(
                    url = if (item.showAvatar) {
                        item.senderAvatar
                    } else "",
                    size = 26.dp,
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(start = 20.dp, end = 0.dp, top = 12.dp)
            ) {
                if (item.showAvatar)
                    Presence(presence = presence)
            }
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

            when (item.getTypeMessage()) {
                TypeMessage.TEXT -> TextMessage(maxWidth, item)
                TypeMessage.IMAGE -> Box {}
                TypeMessage.VIDEO -> Box {}
                TypeMessage.AUDIO -> Box {}
            }
        }
    }
}


@Composable
private fun TextMessage(maxWidth: Dp, item: MessageModel) {
    val content = @Composable {
        when (item.getStatusMessage()) {
            TYPING -> Column(
                horizontalAlignment = Alignment.End
            ) {
                GifImage(
                    data = R.drawable.anim_typing_message, modifier = Modifier.size(24.dp),
                )
            }

            else -> Column(
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
                        TimeStamp(item.getDateTimeMessage())
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
private fun TimeStamp(date: Date) {
    Text(
        text = DateFormatUtil.getFormattedDate(date, format = DateFormatUtil.TIME_FORMAT2),
        style = TextStyle(
            fontSize = 8.sp,
            color = Color191919.copy(alpha = 0.6F),
        ),
    )
}

@Composable
private fun StatusMessage(type: String) {
    val statusComposable = @Composable {
        when (type.lowercase()) {
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