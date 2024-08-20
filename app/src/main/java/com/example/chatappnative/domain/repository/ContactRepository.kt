package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.data.model.PagedListModel
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getContactList(
        page: Int = 1,
        pageSize: Int = 15,
        keyword: String? = null,
    ): Flow<ResponseState<PagedListModel<ContactModel>>>
}