package com.example.chatappnative.data.data_source

import com.example.chatappnative.core.constants.NetworkUrl.GET_FRIEND_LIST
import com.example.chatappnative.core.constants.NetworkUrl.GET_USER_LIST
import com.example.chatappnative.core.constants.NetworkUrl.UPDATE_FRIEND_STATUS
import com.example.chatappnative.data.api.BaseResponse
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.data.model.FriendModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.UpdateFriendStatusModel
import com.example.chatappnative.data.param.UpdateFriendStatusParam
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ContactDataSource {

    @GET(GET_FRIEND_LIST)
    suspend fun getFriendList(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 15,
        @Query("keyword") keyword: String? = null,
        @Query("exceptFriendIds") exceptFriendIds: String? = null,
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<PagedListModel<FriendModel>>>

    @GET(GET_USER_LIST)
    suspend fun getContactList(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 15,
        @Query("keyword") keyword: String? = null,
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<PagedListModel<ContactModel>>>

    @POST(UPDATE_FRIEND_STATUS)
    suspend fun updateFriendStatus(
        @Body postData: UpdateFriendStatusParam,
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<UpdateFriendStatusModel>>
}