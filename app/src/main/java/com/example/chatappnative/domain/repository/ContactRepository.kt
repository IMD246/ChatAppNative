package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.data.model.FriendModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.UpdateFriendStatusModel
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getFriendList(
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
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