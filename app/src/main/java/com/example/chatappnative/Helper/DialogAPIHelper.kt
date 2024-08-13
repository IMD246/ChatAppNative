package com.example.chatappnative.Helper

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.size.Size
import com.example.chatappnative.R
import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.presentation.auth.composables.GifImage
import com.example.chatappnative.ui.theme.ColorF2F2F2

class DialogAPIHelper {
    private val isShowDialog = mutableStateOf(false)
    private val responseState: MutableState<ResponseState<*>?> = mutableStateOf(null)

    fun showDialog(stateAPI: ResponseState<*>) {
        isShowDialog.value = true
        responseState.value = stateAPI
    }

    fun hideDialog() {
        isShowDialog.value = false
        responseState.value = null
    }

    fun getIsShowDialog(): Boolean {
        return isShowDialog.value
    }

    @Composable
    fun displayDialog(
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

            else -> {}
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
        val message: String = message.ifEmpty {
            "Success"
        }

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
                    .padding(horizontal = 30.dp)
                    .background(color = ColorF2F2F2, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GifImage(data = R.drawable.anim_success, size = Size(width = 200, height = 200))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
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
                    .padding(horizontal = 30.dp)
                    .background(color = ColorF2F2F2, shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    GifImage(data = R.drawable.anim_error, size = Size(width = 200, height = 200))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Red
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}