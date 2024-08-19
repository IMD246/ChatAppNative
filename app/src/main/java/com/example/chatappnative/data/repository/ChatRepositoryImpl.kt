package com.example.chatappnative.data.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.data_source.ChatDataSource
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.ChatModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource,
    private val preferences: Preferences,
) : ChatRepository {

    override suspend fun getChatList(
        userID: String,
        page: Int,
        pageSize: Int,
        keyword: String?,
    ): Flow<ResponseState<PagedListModel<ChatModel>>> {
        return BaseRepository.callAPI {
            chatDataSource.getChatList(
                userID = userID,
                page = page,
                pageSize = pageSize,
                keyword = keyword,
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }
}