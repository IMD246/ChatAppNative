package com.example.chatappnative.data.data_source

import com.example.chatappnative.core.constants.NetworkUrl.LOGIN
import com.example.chatappnative.core.constants.NetworkUrl.REGISTER
import com.example.chatappnative.data.BaseResponse
import com.example.chatappnative.data.model.UserInfoAccessModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthDataSource {
    @POST(REGISTER)
    suspend fun register(
        @Body() postData: Map<String, String>,
    ): Response<BaseResponse<UserInfoAccessModel>>

    @POST(LOGIN)
    suspend fun login(
        @Body() postData: Map<String, String>,
    ): Response<BaseResponse<UserInfoAccessModel>>
}