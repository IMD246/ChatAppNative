package com.example.chatappnative.presentation.main.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatappnative.presentation.composables.BaseSearchBar
import com.example.chatappnative.presentation.main.contact.components.ContactContent
import com.example.chatappnative.ui.theme.ColorPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(contactModel: ContactViewModel) {
    val isRefreshing = contactModel.isRefreshing.collectAsState().value

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            contactModel.onRefresh()
        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AppBar(contactModel)
                ContactContent(contactModel)
            }
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