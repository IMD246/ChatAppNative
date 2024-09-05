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
) {
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
            val totalOffset = lastVisibleItem.size
            val currentOffset = listState.firstVisibleItemScrollOffset

            val totalContentHeight = (layoutInfo.visibleItemsInfo.sumOf { it.size } +
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.offset!!)

            // Check if all items are visible
            totalContentHeight + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset)

            Log.d(
                "BaseList",
                "viewportHeight: $viewportHeight, totalOffset: $totalOffset, currentOffset: $currentOffset: "
            )

            if (totalOffset <= viewportHeight) return@derivedStateOf false

            if (currentOffset + viewportHeight >= totalContentHeight - rangeLoadMore) return@derivedStateOf true

            return@derivedStateOf false
        }
    }

//    val shouldLoadMore = remember {
//        derivedStateOf {
//            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
//                ?: return@derivedStateOf true
//
//            lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1 && listState.layoutInfo.totalItemsCount - 1 >= APIConstants.PAGE_SIZE_LOAD_MORE
//        }
//    }

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