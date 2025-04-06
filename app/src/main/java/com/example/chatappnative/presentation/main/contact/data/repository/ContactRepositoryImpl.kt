package com.example.chatappnative.presentation.main.contact.data.repository

import com.example.chatappnative.gateway.api.PagedListModel
import com.example.chatappnative.gateway.api.ResponseState
import com.example.chatappnative.presentation.main.contact.data.data_source.ContactDataSource
import com.example.chatappnative.gateway.local_database.Preferences
import com.example.chatappnative.gateway.api.BaseRepository
import com.example.chatappnative.presentation.main.contact.data.domain.entity.FriendEntity
import com.example.chatappnative.presentation.main.contact.data.domain.repository.ContactRepository
import com.example.chatappnative.presentation.main.contact.data.domain.entity.UpdateFriendStatusEntity
import com.example.chatappnative.presentation.main.contact.data.domain.entity.ContactEntity
import com.example.chatappnative.presentation.main.contact.data.param.UpdateFriendStatusParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val contactDataSource: ContactDataSource,
    private val preferences: Preferences,
) : ContactRepository {

    override suspend fun getFriendList(
        page: Int,
        pageSize: Int,
        keyword: String?,
        exceptFriendIds: String?,
    ): Flow<ResponseState<PagedListModel<FriendEntity>>> {
        return BaseRepository.callAPI {
            contactDataSource.getFriendList(
                page = page,
                pageSize = pageSize,
                keyword = keyword,
                exceptFriendIds = exceptFriendIds,
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
                        data = it.data?.data?.map { e -> e.toEntity() }?.toList() ?: emptyList(),
                        currentPage = it.data?.currentPage ?: 0,
                        pageSize = pageSize,
                        total = it.data?.total ?: 0,
                        totalPages = it.data?.totalPages ?: 0,
                    ),
                        it.message
                    )
                }
                is ResponseState.Error -> {
                    ResponseState.Error(null, it.message ?: "")
                }
            }
        }
    }

    override suspend fun getContactList(
        page: Int,
        pageSize: Int,
        keyword: String?
    ): Flow<ResponseState<PagedListModel<ContactEntity>>> {
        return BaseRepository.callAPI {
            contactDataSource.getContactList(
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
                        data = it.data?.data?.map { e -> e.toEntity() }?.toList() ?: emptyList(),
                        currentPage = it.data?.currentPage ?: 0,
                        pageSize = pageSize,
                        total = it.data?.total ?: 0,
                        totalPages = it.data?.totalPages ?: 0,
                    ),
                        it.message
                    )
                }
                is ResponseState.Error -> {
                    ResponseState.Error(null, it.message ?: "")
                }
            }
        }
    }

    override suspend fun updateFriendStatus(
        friendId: String,
        status: Int
    ): Flow<ResponseState<UpdateFriendStatusEntity>> {
        return BaseRepository.callAPI {
            contactDataSource.updateFriendStatus(
                postData = UpdateFriendStatusParam(
                    friendId = friendId,
                    status = status
                ),
                accessToken = "Bearer ${preferences.getAccessToken()}"
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
}