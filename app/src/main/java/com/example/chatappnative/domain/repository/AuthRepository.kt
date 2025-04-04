package com.example.chatappnative.domain.repository

import com.example.chatappnative.gateway.api.ResponseState
import com.example.chatappnative.gateway.param.LoginParam
import com.example.chatappnative.gateway.param.RegisterParam
import com.example.chatappnative.domain.entity.RefreshDeviceTokenEntity
import com.example.chatappnative.domain.entity.RefreshTokenEntity
import com.example.chatappnative.domain.entity.UserInfoEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(
        registerParam: RegisterParam,
    ): Flow<ResponseState<UserInfoEntity>>

    suspend fun login(
        loginParam: LoginParam,
    ): Flow<ResponseState<UserInfoEntity>>

    suspend fun refreshDeviceToken(
        deviceToken: String,
    ): Flow<ResponseState<RefreshDeviceTokenEntity>>

    suspend fun logout(): Flow<ResponseState<Boolean>>

    suspend fun refreshToken(
        refreshToken: String
    ): Flow<ResponseState<RefreshTokenEntity>>
}