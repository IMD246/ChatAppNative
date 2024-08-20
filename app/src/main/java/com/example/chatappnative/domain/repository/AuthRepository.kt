package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.model.UserInfoAccessModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(
        name: String,
        email: String,
        password: String,
        deviceToken: String,
    ): Flow<ResponseState<UserInfoAccessModel>>

    suspend fun login(
        email: String,
        password: String,
        deviceToken: String,
    ): Flow<ResponseState<UserInfoAccessModel>>
}