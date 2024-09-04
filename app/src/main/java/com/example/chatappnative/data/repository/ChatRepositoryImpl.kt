package com.example.chatappnative.data.repository

import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.data_source.ChatDataSource
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.ChatDetailModel
import com.example.chatappnative.data.model.ChatDetailParamModel
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
        page: Int,
        pageSize: Int,
        keyword: String?,
    ): Flow<ResponseState<PagedListModel<ChatModel>>> {
        return BaseRepository.callAPI {
            chatDataSource.getChatList(
                page = page,
                pageSize = pageSize,
                keyword = keyword,
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }

    override suspend fun getChatDetail(
        pageSizeMessage: Int,
        chatID: String?,
        listUserID: List<String>?,
        type: String,
    ): Flow<ResponseState<ChatDetailModel>> {
        return BaseRepository.callAPI {
            chatDataSource.getChatDetail(
                postData = ChatDetailParamModel(
                    pageSizeMessage = pageSizeMessage,
                    chatID = chatID,
                    listUserID = listUserID,
                    type = type,
                ),
//                accessToken = "Bearer ${preferences.getAccessToken()}"
                accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2YmEzNjY2Zjk5M2E4NDVhZmI2MzVlOSIsImlhdCI6MTcyNTQ2MzYzNywiZXhwIjoxNzI1NDY3MjM3fQ.26BeFf7uEZim_x7AYz5vb-kMEPRJdWe9CmxBCCyTniU"
            )
        }
    }
}