package com.example.chatappnative.gateway.repository

import com.example.chatappnative.gateway.api.ResponseState
import com.example.chatappnative.gateway.local_database.Preferences
import com.example.chatappnative.gateway.model.ChatDetailModel
import com.example.chatappnative.gateway.model.ChatModel
import com.example.chatappnative.gateway.model.MessageModel
import com.example.chatappnative.gateway.model.PagedListModel
import com.example.chatappnative.gateway.param.ChatDetailParam
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
                postData = ChatDetailParam(
                    pageSizeMessage = pageSizeMessage,
                    chatID = chatID,
                    listUserID = listUserID,
                    type = type,
                ),
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }

    override suspend fun getChatMessages(
        page: Int,
        chatID: String,
        pageSize: Int
    ): Flow<ResponseState<PagedListModel<MessageModel>>> {
        return BaseRepository.callAPI {
            chatDataSource.getChatMessages(
                page = page,
                chatID = chatID,
                pageSize = pageSize,
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }
}