package com.example.chatappnative.data.data_source

import com.example.chatappnative.core.constants.NetworkUrl.LOGIN
import com.example.chatappnative.core.constants.NetworkUrl.LOGOUT
import com.example.chatappnative.core.constants.NetworkUrl.REFRESH_DEVICE_TOKEN
import com.example.chatappnative.core.constants.NetworkUrl.REFRESH_TOKEN
import com.example.chatappnative.core.constants.NetworkUrl.REGISTER
import com.example.chatappnative.data.api.BaseResponse
import com.example.chatappnative.data.model.RefreshDeviceTokenModel
import com.example.chatappnative.data.model.RefreshTokenModel
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.example.chatappnative.data.param.LoginParam
import com.example.chatappnative.data.param.RegisterParam
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthDataSource {
    @POST(REGISTER)
    suspend fun register(
        @Body postData: RegisterParam,
    ): Response<BaseResponse<UserInfoAccessModel>>

    @POST(LOGIN)
    suspend fun login(
        @Body postData: LoginParam,
    ): Response<BaseResponse<UserInfoAccessModel>>

    @POST(REFRESH_DEVICE_TOKEN)
    suspend fun refreshDeviceToken(
        @Body postData: Map<String, String>,
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<RefreshDeviceTokenModel>>

    @GET(LOGOUT)
    suspend fun logout(
        @Header("Authorization") accessToken: String = ""
    ): Response<BaseResponse<Boolean>>

    @POST(REFRESH_TOKEN)
    suspend fun refreshToken(
        @Body postData: Map<String, String>,
    ): Response<BaseResponse<RefreshTokenModel>>
}