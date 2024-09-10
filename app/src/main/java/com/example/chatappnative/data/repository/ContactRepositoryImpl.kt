package com.example.chatappnative.data.repository

import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.data_source.ContactDataSource
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.data.model.FriendModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.UpdateFriendStatusModel
import com.example.chatappnative.data.param.UpdateFriendStatusParam
import com.example.chatappnative.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
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
    ): Flow<ResponseState<PagedListModel<FriendModel>>> {
        return BaseRepository.callAPI {
            contactDataSource.getFriendList(
                page = page,
                pageSize = pageSize,
                keyword = keyword,
                exceptFriendIds = exceptFriendIds,
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }

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
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }

    override suspend fun updateFriendStatus(
        friendId: String,
        status: Int
    ): Flow<ResponseState<UpdateFriendStatusModel>> {
        return BaseRepository.callAPI {
            contactDataSource.updateFriendStatus(
                postData = UpdateFriendStatusParam(
                    friendId = friendId,
                    status = status
                ),
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }
}