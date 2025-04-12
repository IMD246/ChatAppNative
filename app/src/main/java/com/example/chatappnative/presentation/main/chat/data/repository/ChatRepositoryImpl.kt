package com.example.chatappnative.presentation.main.chat.data.repository

import com.example.chatappnative.gateway.api.PagedListModel
import com.example.chatappnative.gateway.api.ResponseState
import com.example.chatappnative.gateway.local_database.Preferences
import com.example.chatappnative.presentation.main.chat.data.param.ChatDetailParam
import com.example.chatappnative.gateway.api.BaseRepository
import com.example.chatappnative.presentation.main.chat.data.data_source.ChatDataSource
import com.example.chatappnative.presentation.main.chat.data.domain.entity.ChatDetailEntity
import com.example.chatappnative.presentation.main.chat.data.domain.entity.ChatEntity
import com.example.chatappnative.presentation.main.chat.data.domain.entity.GroupMessageEntity
import com.example.chatappnative.presentation.main.chat.data.domain.entity.MessageEntity
import com.example.chatappnative.presentation.main.chat.data.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource,
    private val preferences: Preferences,
) : ChatRepository {

    override suspend fun getChatList(
        page: Int,
        pageSize: Int,
        keyword: String?,
    ): Flow<ResponseState<PagedListModel<ChatEntity>>> {
        return BaseRepository.callAPI {
            chatDataSource.getChatList(
                page = page,
                pageSize = pageSize,
                keyword = keyword,
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }.map {
            when (it) {
                is ResponseState.Loading -> {
                    ResponseState.Loading()
                }

                is ResponseState.Success -> {
                    ResponseState.Success(
                        PagedListModel(
                            data = it.data?.data?.map { e -> e.toEntity() }?.toList()
                                ?: emptyList(),
                            currentPage = it.data?.currentPage ?: 0,
                            pageSize = pageSize,
                            total = it.data?.total ?: 0,
                            totalPages = it.data?.totalPages ?: 0,
                        ), it.message
                    )
                }

                is ResponseState.Error -> {
                    ResponseState.Error(null, it.message ?: "")
                }
            }
        }
    }

    override suspend fun getChatDetail(
        pageSizeMessage: Int,
        chatID: String?,
        listUserID: List<String>?,
        type: String,
    ): Flow<ResponseState<ChatDetailEntity>> {
        return BaseRepository.callAPI {
            chatDataSource.getChatDetail(
                postData = ChatDetailParam(
                    pageSizeMessage = pageSizeMessage,
                    chatID = chatID,
                    listUserID = listUserID,
                    type = type,
                ), accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }.map {
                when (it) {
                    is ResponseState.Loading -> {
                        ResponseState.Loading()
                    }

                    is ResponseState.Success -> {
                        ResponseState.Success(it.data!!.toEntity(), it.message)
                    }

                    is ResponseState.Error -> {
                        ResponseState.Error(null, it.message ?: "")
                    }
                }
            }
    }

    override suspend fun getChatMessages(
        page: Int, chatID: String, pageSize: Int
    ): Flow<ResponseState<PagedListModel<GroupMessageEntity>>> {
        return BaseRepository.callAPI {
            chatDataSource.getChatMessages(
                page = page,
                chatID = chatID,
                pageSize = pageSize,
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }.map {
            when (it) {
                is ResponseState.Loading -> {
                    ResponseState.Loading()
                }

                is ResponseState.Success -> {
                    ResponseState.Success(
                        PagedListModel(
                            data = it.data?.data?.map { e -> e.toEntity() }?.toList()
                                ?: emptyList(),
                            currentPage = it.data?.currentPage ?: 0,
                            pageSize = pageSize,
                            total = it.data?.total ?: 0,
                            totalPages = it.data?.totalPages ?: 0,
                        ), it.message
                    )
                }

                is ResponseState.Error -> {
                    ResponseState.Error(null, it.message ?: "")
                }
            }
        }
    }
}