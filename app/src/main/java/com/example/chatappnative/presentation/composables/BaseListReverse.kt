package com.example.chatappnative.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chatappnative.R

@Composable
fun <T> BaseListReverse(
    modifier: Modifier = Modifier,
    items: List<T>,
    isLoading: Boolean = false,
    isLoadMore: Boolean = false,
    onLoadMore: (suspend () -> Unit)? = null,
    loadingContent: @Composable () -> Unit?,
    loadMoreContent: @Composable () -> Unit?,
    emptyContent: @Composable () -> Unit?,
    listState: LazyListState = rememberLazyListState(),
    contentItem: @Composable (T) -> Unit,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    keyItem: ((item: T) -> Any)? = null,
    isTyping: Boolean = false,
    triggerScroll: Boolean = false,
    onTriggerScroll: () -> Unit = {},
    rangeLoadMore: Int = 300,
) {
    var triggerScrollToEnd by remember { mutableStateOf(false) }

    LaunchedEffect(triggerScrollToEnd) {
        if (!triggerScrollToEnd) return@LaunchedEffect

        listState.scrollToItem(0)
        triggerScrollToEnd = false
    }

    LaunchedEffect(triggerScroll) {
        if (!triggerScroll) return@LaunchedEffect

        listState.scrollToItem(0)
        onTriggerScroll()
    }

    val loadMoreComposable = @Composable { loadMoreContent() ?: Box {} }

    if (isLoading) {
        return loadingContent() ?: Box {}
    }

    if (items.isEmpty()) {
        return LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                emptyContent() ?: Box {

                }
            }
        }
    }

    val enableButtonScrollToEnd by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            val layoutInfo = listState.layoutInfo
            val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
            val totalOffset = lastVisibleItem.size
            val currentOffset = listState.firstVisibleItemScrollOffset

            if (totalOffset <= viewportHeight) return@derivedStateOf false

            if (currentOffset >= viewportHeight - 200) return@derivedStateOf true

            return@derivedStateOf false
        }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            val layoutInfo = listState.layoutInfo
            val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
            val totalOffset = lastVisibleItem.size
            val currentOffset = listState.firstVisibleItemScrollOffset

            val totalContentHeight = (layoutInfo.visibleItemsInfo.sumOf { it.size } +
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.offset!!)

            // Check if all items are visible
            totalContentHeight + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset)

            Log.d(
                "BaseListReverse",
                "viewportHeight: $viewportHeight, totalOffset: $totalOffset, currentOffset: $currentOffset:, totalContentHeight: $totalContentHeight"
            )

            if (totalOffset <= viewportHeight - 100) return@derivedStateOf false

            if (currentOffset + viewportHeight >= totalContentHeight - rangeLoadMore) return@derivedStateOf true

            return@derivedStateOf false
        }
    }

    if (onLoadMore != null) {
        LaunchedEffect(shouldLoadMore) {
            snapshotFlow { shouldLoadMore }
                .collect {
                    if (it) {
                        onLoadMore()
                    }
                }
        }
    }
    Box {
        LazyColumn(
            modifier = modifier,
            reverseLayout = true,
            state = listState,
        ) {
            items(
                count = items.size,
                contentType = {
                    contentItem
                },
                key = { index ->
                    keyItem?.invoke(items[index]) ?: items[index].toString()
                },
                itemContent = { index ->
                    contentItem(items[index])
                }
            )

            if (isLoadMore) {
                item(contentType = loadMoreComposable) {
                    loadMoreComposable()
                }
            }
        }
        if (enableButtonScrollToEnd)
            FloatingActionButton(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomCenter),
                shape = CircleShape,
                containerColor = Color.White,
                onClick = {
                    triggerScrollToEnd = true
                }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
    }
}