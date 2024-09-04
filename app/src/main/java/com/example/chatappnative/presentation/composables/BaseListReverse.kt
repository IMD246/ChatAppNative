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
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.chatappnative.data.api.APIConstants

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
) {

    LaunchedEffect(isLoadMore) {
        Log.d("BaseListReverse", "isLoadmore: $isLoadMore")
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

    remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - 2)
        }
    }

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true

            lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1 && listState.layoutInfo.totalItemsCount - 1 >= APIConstants.PAGE_SIZE_LOAD_MORE
        }
    }

    if (onLoadMore != null) {
        LaunchedEffect(shouldLoadMore) {
            snapshotFlow { shouldLoadMore.value }
                .collect {
                    if (it) {
                        onLoadMore()
                    }
                }
        }
    }

    return LazyColumn(
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
}