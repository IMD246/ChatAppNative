package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.ResponseState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<ResponseState<Boolean>>
}