package com.example.chatappnative.helper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.size.Size
import com.example.chatappnative.R
import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.presentation.composables.GifImage
import com.example.chatappnative.ui.theme.ColorF2F2F2
import java.util.Timer
import kotlin.concurrent.schedule

class DialogAPIHelper {
    private val isShowDialog = mutableStateOf(false)
    private val responseState: MutableState<ResponseState<*>?> = mutableStateOf(null)

    fun showDialog(stateAPI: ResponseState<*>) {
        isShowDialog.value = true
        responseState.value = stateAPI

        if (stateAPI is ResponseState.Error || stateAPI is ResponseState.Success) {
            Timer().schedule(2000L) {
                cancel()
                hideDialog()
            }
        }
    }

    fun hideDialog() {
        isShowDialog.value = false
        responseState.value = null
    }

    fun getIsShowDialog(): Boolean {
        return isShowDialog.value
    }

    fun getResponseState(): ResponseState<*>? {
        return responseState.value
    }

    @Composable
    fun DisplayDialog(
        dismissOnBackPress: Boolean = false,
        dismissOnClickOutside: Boolean = false,
        needShowSuccess: Boolean = true,
    ) {
        if (!isShowDialog.value || responseState.value == null)
            return

        when (responseState.value) {
            is ResponseState.Loading -> {
                LoadingDialog(dismissOnBackPress, dismissOnClickOutside)
            }

            is ResponseState.Success -> {
                if (needShowSuccess) {
                    SuccessDialog(
                        message = (responseState.value as ResponseState.Success<*>).message ?: ""
                    )
                }
            }

            is ResponseState.Error -> {
                ErrorDialog(
                    message = (responseState.value as ResponseState.Error<*>).message.toString()
                )
            }

            else -> {
                return
            }
        }
    }

    @Composable
    fun LoadingDialog(
        dismissOnBackPress: Boolean = false,
        dismissOnClickOutside: Boolean = false,
    ) {
        Dialog(
            properties = DialogProperties(
                dismissOnClickOutside = dismissOnClickOutside,
                dismissOnBackPress = dismissOnBackPress,
            ),
            onDismissRequest = {
                hideDialog()
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .height(150.dp)
                    .padding(horizontal = 30.dp)
                    .background(color = ColorF2F2F2, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                GifImage(data = R.drawable.anim_loading)
            }
        }
    }

    @Composable
    fun SuccessDialog(
        dismissOnBackPress: Boolean = false,
        dismissOnClickOutside: Boolean = false,
        message: String = "",
    ) {
        Dialog(
            properties = DialogProperties(
                dismissOnClickOutside = dismissOnClickOutside,
                dismissOnBackPress = dismissOnBackPress,
            ),
            onDismissRequest = {
                hideDialog()
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .defaultMinSize(minHeight = 150.dp)
                    .padding(horizontal = 30.dp)
                    .background(color = ColorF2F2F2, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GifImage(data = R.drawable.anim_success, size = Size(width = 200, height = 200))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.Green
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    @Composable
    fun ErrorDialog(
        dismissOnBackPress: Boolean = false,
        dismissOnClickOutside: Boolean = false,
        message: String = "",
    ) {
        Dialog(
            properties = DialogProperties(
                dismissOnClickOutside = dismissOnClickOutside,
                dismissOnBackPress = dismissOnBackPress,
            ),
            onDismissRequest = {
                hideDialog()
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .defaultMinSize(minHeight = 150.dp)
                    .padding(horizontal = 30.dp)
                    .background(color = ColorF2F2F2, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GifImage(data = R.drawable.anim_error, size = Size(width = 200, height = 200))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Red
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}