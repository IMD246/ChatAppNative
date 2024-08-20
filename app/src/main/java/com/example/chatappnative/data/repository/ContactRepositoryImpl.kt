package com.example.chatappnative.data.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.data_source.ContactDataSource
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val contactDataSource: ContactDataSource,
    private val preferences: Preferences,
) : ContactRepository {

    override suspend fun getContactList(
        page: Int,
        pageSize: Int,
        keyword: String?
    ): Flow<ResponseState<PagedListModel<ContactModel>>> {
        return BaseRepository.callAPI {
            contactDataSource.getContactList(
                page = page,
                pageSize = pageSize,
                keyword = keyword,
//                accessToken = "Bearer ${preferences.getAccessToken()}"
                accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2YjhjNjA4YjFmODUwNzU1NzcwMDg3ZCIsImlhdCI6MTcyMzM4ODAyMX0.X7bLhNUuRmNlhSP21ciiAwKLPBFTzsPT-GC_9uCqZbw"
            )
        }
    }
}