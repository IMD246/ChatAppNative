package com.example.chatappnative.presentation.main.chat.data.domain.repository

import com.example.chatappnative.gateway.api.PagedListModel
import com.example.chatappnative.gateway.api.ResponseState
import com.example.chatappnative.presentation.main.chat.data.domain.entity.ChatDetailEntity
import com.example.chatappnative.presentation.main.chat.data.domain.entity.ChatEntity
import com.example.chatappnative.presentation.main.chat.data.domain.entity.GroupMessageEntity
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatList(
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
    ): Flow<ResponseState<PagedListModel<ChatEntity>>>

    suspend fun getChatDetail(
        pageSizeMessage: Int = 15,
        chatID: String? = null,
        listUserID: List<String>? = null,
        type: String = "personal",
    ): Flow<ResponseState<ChatDetailEntity>>

    suspend fun getChatMessages(
        page: Int = 1,
        chatID: String = "",
        pageSize: Int = 15,
    ): Flow<ResponseState<PagedListModel<GroupMessageEntity>>>
}