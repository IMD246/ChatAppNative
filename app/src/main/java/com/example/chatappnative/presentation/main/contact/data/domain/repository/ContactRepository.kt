package com.example.chatappnative.presentation.main.contact.data.domain.repository

import com.example.chatappnative.gateway.api.ResponseState
import com.example.chatappnative.gateway.api.PagedListModel
import com.example.chatappnative.presentation.main.contact.data.domain.entity.FriendEntity
import com.example.chatappnative.presentation.main.contact.data.domain.entity.UpdateFriendStatusEntity
import com.example.chatappnative.presentation.main.contact.data.domain.entity.ContactEntity
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getFriendList(
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
        exceptFriendIds: String? = null,
    ): Flow<ResponseState<PagedListModel<FriendEntity>>>

    suspend fun getContactList(
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
    ): Flow<ResponseState<PagedListModel<ContactEntity>>>

    suspend fun updateFriendStatus(
        friendId: String,
        status: Int,
    ): Flow<ResponseState<UpdateFriendStatusEntity>>
}