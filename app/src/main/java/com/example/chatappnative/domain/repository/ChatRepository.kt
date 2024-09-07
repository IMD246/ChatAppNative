package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.model.ChatDetailModel
import com.example.chatappnative.data.model.ChatModel
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.model.PagedListModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChatList(
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
    ): Flow<ResponseState<PagedListModel<ChatModel>>>

    suspend fun getChatDetail(
        pageSizeMessage: Int = 15,
        chatID: String? = null,
        listUserID: List<String>? = null,
        type: String = "personal",
    ): Flow<ResponseState<ChatDetailModel>>

    suspend fun getChatMessages(
        page: Int = 1,
        chatID: String = "",
        pageSize: Int = 15,
    ): Flow<ResponseState<PagedListModel<MessageModel>>>
}