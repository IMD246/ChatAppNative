package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.model.RefreshDeviceTokenModel
import com.example.chatappnative.data.model.RefreshTokenModel
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.example.chatappnative.data.param.LoginParam
import com.example.chatappnative.data.param.RegisterParam
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(
        registerParam: RegisterParam,
    ): Flow<ResponseState<UserInfoAccessModel>>

    suspend fun login(
        loginParam: LoginParam,
    ): Flow<ResponseState<UserInfoAccessModel>>

    suspend fun refreshDeviceToken(
        deviceToken: String,
    ): Flow<ResponseState<RefreshDeviceTokenModel>>

    suspend fun logout(): Flow<ResponseState<Boolean>>

    suspend fun refreshToken(
        refreshToken: String
    ): Flow<ResponseState<RefreshTokenModel>>
}