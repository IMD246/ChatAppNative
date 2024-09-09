package com.example.chatappnative.presentation.message

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatappnative.R
import com.example.chatappnative.event.UpdateUserPresenceEvent
import com.example.chatappnative.presentation.composables.ObserverAsEvent
import com.example.chatappnative.presentation.message.components.AppBarMessage
import com.example.chatappnative.presentation.message.components.MessageContent
import com.example.chatappnative.presentation.message.components.MessageInput
import com.example.chatappnative.service.EventBusService
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class MessageActivity : ComponentActivity() {
    private val messageModel: MessageViewModel by viewModels()

    companion object {
        const val CHAT_PARAMS = "chat_params"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatAppNativeTheme {
                MessageScreen(messageViewModel = messageModel, context = this)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateUserPresenceEvent(event: UpdateUserPresenceEvent) {
        messageModel.updateItemPresence(
            event.userPresenceSocketModel
        )
    }

    override fun onStart() {
        super.onStart()
        EventBusService.register(this)
    }

    override fun onStop() {
        EventBusService.unregister(this)
        super.onStop()
    }

    override fun onDestroy() {
        messageModel.onLeaveRoom()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    messageViewModel: MessageViewModel,
    context: Context,
) {
    val dialogAPIHelper = messageViewModel.dialogAPIHelper

    dialogAPIHelper.DisplayDialog()

    val channelToastFlow = messageViewModel.messageErrorFlow

    ObserverAsEvent(channelToastFlow) { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    val isRefreshing = messageViewModel.isRefreshing.collectAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            messageViewModel.onRefresh()
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_bg_message),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AppBarMessage(messageViewModel)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 20.dp),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    MessageContent(messageViewModel)
                }
                MessageInput(messageViewModel)
            }
        }
    }
}