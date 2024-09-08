package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.model.DeviceTokenModel
import com.example.chatappnative.data.model.RefreshTokenModel
import com.example.chatappnative.data.model.UserInfoAccessModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(
        name: String,
        email: String,
        phone: String = "",
        password: String,
        deviceToken: String,
    ): Flow<ResponseState<UserInfoAccessModel>>

    suspend fun login(
        email: String,
        password: String,
        deviceToken: String,
    ): Flow<ResponseState<UserInfoAccessModel>>

    suspend fun refreshDeviceToken(
        deviceToken: String,
    ): Flow<ResponseState<DeviceTokenModel>>

    suspend fun logout(): Flow<ResponseState<Boolean>>

    suspend fun refreshToken(
        refreshToken: String
    ): Flow<ResponseState<RefreshTokenModel>>
}