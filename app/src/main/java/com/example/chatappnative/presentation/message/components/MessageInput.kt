package com.example.chatappnative.presentation.message.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatappnative.R
import com.example.chatappnative.presentation.composables.BaseInput
import com.example.chatappnative.presentation.message.MessageViewModel
import com.example.chatappnative.ui.theme.ColorBlack
import com.example.chatappnative.ui.theme.ColorF9FFFF
import com.example.chatappnative.ui.theme.ColorPrimary
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MessageInput(messageViewModel: MessageViewModel) {
    val messageText = messageViewModel.messageText.collectAsState().value

    val permissionState =
        rememberPermissionState(permission = android.Manifest.permission.READ_EXTERNAL_STORAGE)

    val firstTime = remember { mutableStateOf(true) }

    LaunchedEffect(permissionState.status) {
        withContext(Dispatchers.IO) {
            if (permissionState.status.isGranted) {
                messageViewModel.initMediaList()
                if (!firstTime.value) {
                    messageViewModel.onOpenPhotoSheet()
                }
                if (firstTime.value) {
                    firstTime.value = false
                }
            }
        }
    }

    val actionMessageIcon = if (messageText.isEmpty()) {
        R.drawable.ic_audio
    } else {
        R.drawable.ic_send
    }

    Row(
        modifier = Modifier.padding(
            start = 16.dp,
            end = 16.dp,
        ),
        horizontalArrangement = Arrangement.Center
    ) {
        BaseInput(
            value = messageText,
            onValueChange = messageViewModel.onChangedMessageText,
            hint = "Message",
            onValidation = { "" },
            singleLine = false,
            maxLines = 5,
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = 16.dp),
            prefix = {
                Row {
                    IconButton(
                        modifier = Modifier.size(17.dp),
                        onClick = {

                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_emote),
                            contentDescription = "emote"
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
            },
            suffix = {
                Row {
                    IconButton(
                        modifier = Modifier.size(18.dp),
                        onClick = {

                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_attachment),
                            contentDescription = "attachment"
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {

                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "camera",
                            modifier =
                            Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        modifier = Modifier.size(20.dp),
                        onClick = {
                            if (!permissionState.status.isGranted) {
                                permissionState.launchPermissionRequest()
                                firstTime.value = false
                            } else {
                                messageViewModel.onOpenPhotoSheet()
                            }
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_photo),
                            contentDescription = "photo",
                            modifier =
                            Modifier.size(16.dp)
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = ColorBlack.copy(0.05F),
                unfocusedIndicatorColor = ColorBlack.copy(0.05F),
                unfocusedContainerColor = ColorF9FFFF,
                focusedContainerColor = ColorF9FFFF,
            ),
        )
        Column {
            Spacer(modifier = Modifier.height(2.dp))
            Surface(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = {
                            if (messageText.isNotEmpty() && messageText.isNotBlank()) {
                                messageViewModel.onSend()
                            } else {
                                messageViewModel.onAudio()
                            }
                        },
                    ),
                color = ColorPrimary
            ) {
                Icon(
                    painter = painterResource(id = actionMessageIcon),
                    contentDescription = "",
                    tint = ColorF9FFFF,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}