package com.example.chatappnative.presentation.main.chat.data.data_source

import com.example.chatappnative.core.constants.NetworkUrl.GET_CHAT_LIST
import com.example.chatappnative.core.constants.NetworkUrl.TAKE_MESSAGE_LIST
import com.example.chatappnative.core.constants.NetworkUrl.TAKE_ROOM_CHAT
import com.example.chatappnative.gateway.api.BaseResponse
import com.example.chatappnative.gateway.api.PagedListModel
import com.example.chatappnative.presentation.main.chat.data.model.ChatDetailModel
import com.example.chatappnative.presentation.main.chat.data.model.ChatModel
import com.example.chatappnative.presentation.main.chat.data.model.GroupMessageModel
import com.example.chatappnative.presentation.main.chat.data.param.ChatDetailParam
import com.example.chatappnative.presentation.main.chat.data.model.MessageModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatDataSource {

    @GET(GET_CHAT_LIST)
    suspend fun getChatList(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 15,
        @Query("keyword") keyword: String? = null,
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<PagedListModel<ChatModel>>>

    @POST(TAKE_ROOM_CHAT)
    suspend fun getChatDetail(
        @Body postData: ChatDetailParam,
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<ChatDetailModel>>

    @GET(TAKE_MESSAGE_LIST)
    suspend fun getChatMessages(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 15,
        @Query("chatID") chatID: String = "",
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<PagedListModel<GroupMessageModel>>>
}