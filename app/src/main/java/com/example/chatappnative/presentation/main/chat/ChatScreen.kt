package com.example.chatappnative.presentation.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.presentation.composables.BaseSearchBar
import com.example.chatappnative.presentation.main.chat.components.CallContent
import com.example.chatappnative.presentation.main.chat.components.ChatContent
import com.example.chatappnative.ui.theme.ColorPrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatModel: ChatViewModel) {
    val selectedTabbarIndex = chatModel.selectedTabbarIndex.collectAsState().value

    val tabs = chatModel.tabs

    val pagerState = rememberPagerState {
        tabs.size
    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            pagerState.isScrollInProgress
            pagerState.currentPage != selectedTabbarIndex

        }.collect {
            chatModel.onUpdateSelectedTabbarIndex(pagerState.currentPage)
        }
    }

    LaunchedEffect(selectedTabbarIndex) {
        if (pagerState.currentPage != selectedTabbarIndex) {
            pagerState.animateScrollToPage(selectedTabbarIndex)
        }
    }

    val isRefreshing = chatModel.isRefreshing.collectAsState().value

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            chatModel.onRefresh()
        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AppBar(chatModel)
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                ) { index ->
                    when (index) {
                        0 -> {
                            ChatContent(chatModel)
                        }

                        1 -> {
                            CallContent(chatModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppBar(chatModel: ChatViewModel) {
    val selectedTabbarIndex = chatModel.selectedTabbarIndex.collectAsState().value

    val tabs = chatModel.tabs

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
                onSubmitted = chatModel.onSubmitted,
            )
            TabRow(
                selectedTabIndex = selectedTabbarIndex,
                containerColor = ColorPrimary,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(text = {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = (if (selectedTabbarIndex == index) {
                                    Color.White
                                } else {
                                    Color.White.copy(alpha = 0.75F)
                                })
                            ),
                        )
                    },
                        selected = selectedTabbarIndex == index,
                        onClick = {
                            chatModel.onUpdateSelectedTabbarIndex(index)
                        }
                    )
                }
            }
        }
    }
}