package com.example.chatappnative.presentation.main.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.R
import com.example.chatappnative.data.model.ChatModel
import com.example.chatappnative.presentation.composables.BaseList
import com.example.chatappnative.presentation.composables.NetworkImage
import com.example.chatappnative.presentation.composables.Presence
import com.example.chatappnative.presentation.main.chat.ChatViewModel
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.util.DateFormatUtil
import kotlinx.coroutines.delay

@Composable
fun ChatContent(chatModel: ChatViewModel) {
    val items = chatModel.chatList.collectAsState().value
    val isLoading = chatModel.isLoadingChatList.collectAsState().value
    val isLoadMore = chatModel.isChatListLoadMore.collectAsState().value

    BaseList(
        items = items,
        loadingContent = {
            ChatListShimmer()
        },
        loadMoreContent = {
            ChatShimmerItem()
        },
        emptyContent = {
            Text(
                text = "There is no conversion here \n" +
                        "lets have some",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color191919.copy(alpha = 0.95F),
                    textAlign = TextAlign.Center,
                )
            )
        },
        isLoading = isLoading,
        contentItem = {
            ChatItem(it)
        },
        isLoadMore = isLoadMore,
        onLoadMore = {
            chatModel.loadMore()
        },
        modifier = Modifier.padding(20.dp, 15.dp),
    )
}

@Composable
private fun ChatItem(item: ChatModel) {
    val dateDisplay: MutableState<String> = remember {
        mutableStateOf(DateFormatUtil.dateMessageFormat(item.getDateTimeLastMessage()))
    }

    LaunchedEffect(Unit) {
        while (true) {
            if (DateFormatUtil.isDiffSecondsMoreThanAMinute(item.getDateTimeLastMessage())) break;

            dateDisplay.value = DateFormatUtil.dateMessageFormat(item.getDateTimeLastMessage())
            delay(1000L)
        }
    }

    Row(
        modifier = Modifier.padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            NetworkImage(
                url = item.urlImage,
            )
            Presence(isPresence = item.presence, date = item.presenceTimestamp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = item.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color191919.copy(alpha = 0.95F),
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Message(item)
        }
        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = dateDisplay.value,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color191919.copy(alpha = 0.6F),
            ),
        )
    }
}

@Composable
private fun Message(item: ChatModel) {
    when (item.typeMessage) {
        "image" -> Row {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.ic_photo),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Photo",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color191919.copy(alpha = 0.6F),
                )
            )
        }

        "video" -> Row {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.ic_video),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Video",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color191919.copy(alpha = 0.6F),
                )
            )
        }

        "record" -> Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                painter = painterResource(id = R.drawable.ic_record),
                contentDescription = "",
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Record",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color191919.copy(alpha = 0.6F),
                )
            )
        }

        else -> Text(
            text = item.lastMessage,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color191919.copy(alpha = 0.6F),
            )
        )
    }
}