package com.example.chatappnative.domain.repository

import com.example.chatappnative.gateway.api.ResponseState
import com.example.chatappnative.gateway.model.ContactModel
import com.example.chatappnative.gateway.model.FriendModel
import com.example.chatappnative.gateway.model.PagedListModel
import com.example.chatappnative.gateway.model.UpdateFriendStatusModel
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getFriendList(
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
        exceptFriendIds: String? = null,
    ): Flow<ResponseState<PagedListModel<FriendModel>>>

    suspend fun getContactList(
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
    ): Flow<ResponseState<PagedListModel<ContactModel>>>

    suspend fun updateFriendStatus(
        friendId: String,
        status: Int,
    ): Flow<ResponseState<UpdateFriendStatusModel>>
}