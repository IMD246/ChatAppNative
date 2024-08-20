package com.example.chatappnative.data.data_source

import com.example.chatappnative.core.constants.NetworkUrl.GET_FRIEND_LIST
import com.example.chatappnative.data.BaseResponse
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.data.model.PagedListModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ContactDataSource {

    @GET(GET_FRIEND_LIST)
    suspend fun getContactList(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 15,
        @Query("keyword") keyword: String? = null,
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<PagedListModel<ContactModel>>>
}