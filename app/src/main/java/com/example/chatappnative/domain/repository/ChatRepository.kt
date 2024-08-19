package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.model.ChatModel
import com.example.chatappnative.data.model.PagedListModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatList(
        userID: String,
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
    ): Flow<ResponseState<PagedListModel<ChatModel>>>
}