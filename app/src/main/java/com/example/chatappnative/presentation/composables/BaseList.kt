package com.example.chatappnative.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun <T> BaseList(
    modifier: Modifier = Modifier,
    items: List<T>,
    isLoading: Boolean = false,
    isLoadMore: Boolean = false,
    onLoadMore: (suspend () -> Unit?)? = null,
    loadingContent: @Composable () -> Unit?,
    loadMoreContent: @Composable () -> Unit?,
    emptyContent: @Composable () -> Unit?,
    listState: LazyListState = rememberLazyListState(),
    contentItem: @Composable (T) -> Unit,
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    keyItem: ((item: T) -> Any)? = null,
    rangeLoadMore: Int = 300,
    isGroupByList: Boolean = false,
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val loadMoreComposable = @Composable { loadMoreContent() ?: Box {} }

    if (isLoading) {
        return loadingContent() ?: Box {}
    }

    if (items.isEmpty()) {
        return LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                emptyContent() ?: Box {

                }
            }
        }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            val layoutInfo = listState.layoutInfo
            val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset
            val currentOffset = listState.firstVisibleItemScrollOffset

            val totalVisibleItemHeight = layoutInfo.visibleItemsInfo.sumOf { it.size }
            val totalItemsCount = layoutInfo.totalItemsCount

            if (totalVisibleItemHeight < screenHeight - 50) return@derivedStateOf false

            if (isGroupByList) {
                Log.d(
                    "BaseList",
                    "totalItemsCount: ${totalItemsCount - 1}, lastVisibleItemIndex: ${lastVisibleItem.index} "
                )

                Log.d(
                    "BaseList",
                    "currentOffset: $currentOffset, viewportHeight: $viewportHeight, totalVisibleItemHeight: ${totalVisibleItemHeight - rangeLoadMore} "
                )

                if (totalItemsCount - 1 == lastVisibleItem.index) {
                    return@derivedStateOf (currentOffset + viewportHeight >= totalVisibleItemHeight - rangeLoadMore)
                }

                return@derivedStateOf false
            } else {
                return@derivedStateOf (totalItemsCount - 3 == lastVisibleItem.index + 1)
            }
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

    return LazyColumn(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        verticalArrangement = verticalArrangement,
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
}