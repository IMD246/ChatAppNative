package com.example.chatappnative.presentation.main.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatappnative.presentation.auth.login.LoginActivity
import com.example.chatappnative.presentation.composables.BaseSearchBar
import com.example.chatappnative.presentation.composables.ObserverAsEvent
import com.example.chatappnative.presentation.main.contact.ContactViewModel
import com.example.chatappnative.ui.theme.ColorPrimary

@Composable
fun SettingScreen(context: Context, settingViewModel: SettingViewModel) {
    val navigateFlow = settingViewModel.navigateChannelFlow

    ObserverAsEvent(navigateFlow) {
        when (it) {
            NavigateSettingScreen.Main -> {
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)

                if (context is Activity) {
                    context.finish()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = {
                settingViewModel.onLogout()
            },
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun AppBar(contactModel: ContactViewModel) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    bottomEnd = 25.dp,
                    bottomStart = 25.dp
                )
            )
            .background(ColorPrimary)
    ) {
        Column {
            Spacer(modifier = Modifier.height(35.dp))
            BaseSearchBar(
                hint = "Search by name, number...",
                onSubmitted = contactModel.onSubmitted,
                onClear = contactModel.onSubmitted
            )
            Spacer(modifier = Modifier.height(35.dp))
        }
    }
}