package com.example.chatappnative.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.util.DateFormatUtil
import kotlinx.coroutines.delay

@SuppressLint("SimpleDateFormat")
@Composable
fun Presence(
    isPresence: Boolean,
    date: String,
) {
    val toLocalDate = DateFormatUtil.parseToLocalDate(date)

    var dateDisplay by remember {
        mutableStateOf(DateFormatUtil.presenceFormat(toLocalDate))
    }

    LaunchedEffect(Unit) {
        while (true) {
            if (DateFormatUtil.isDiffSecondsMoreThanADay(toLocalDate)) break;

            dateDisplay = DateFormatUtil.presenceFormat(toLocalDate)

            delay(DateFormatUtil.getDelayDiffMilliseconds(toLocalDate))
        }
    }

    val presenceComposable: @Composable () -> Unit = {
        if (isPresence) {
            // presence is true
            Box(
                Modifier
                    .size(12.dp)
                    .absoluteOffset(y = (-10).dp, x = 24.dp)
                    .background(
                        color = Color(0xFF48D357),
                        shape = CircleShape,
                    )
            )
        } else {
            // presence is false
            // show date when dateDisplay is not empty
            if (dateDisplay.isNotEmpty()) {
                Box(
                    Modifier
                        .absoluteOffset(y = (-10).dp, x = 24.dp)
                        .background(
                            color = Color(0xFF48D357).copy(alpha = 0.5F),
                            shape = RoundedCornerShape(
                                8.dp,
                            ),
                        )
                        .padding(
                            horizontal = 2.dp,
                            vertical = 0.2.dp
                        )
                ) {
                    Text(
                        text = dateDisplay,
                        color = Color191919,
                        style = TextStyle(fontSize = 8.sp),
                    )
                }
            } else {
                Box(modifier = Modifier.size(0.dp))
            }
        }
    }

    return presenceComposable()
}