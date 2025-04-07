package com.example.chatappnative.presentation.auth.data.repository

import com.example.chatappnative.gateway.api.BaseRepository
import com.example.chatappnative.gateway.api.ResponseState
import com.example.chatappnative.presentation.auth.data.data_source.AuthDataSource
import com.example.chatappnative.gateway.local_database.Preferences
import com.example.chatappnative.presentation.auth.data.domain.entity.RefreshDeviceTokenEntity
import com.example.chatappnative.presentation.auth.data.domain.entity.RefreshTokenEntity
import com.example.chatappnative.presentation.auth.data.param.LoginParam
import com.example.chatappnative.presentation.auth.data.param.RegisterParam
import com.example.chatappnative.presentation.auth.data.domain.repository.AuthRepository
import com.example.chatappnative.presentation.auth.data.domain.entity.UserInfoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val preferences: Preferences,
) : AuthRepository {
    override suspend fun register(
        registerParam: RegisterParam,
    ): Flow<ResponseState<UserInfoEntity>> {
        return BaseRepository.callAPI {
            authDataSource.register(
                postData = registerParam
            )
        }.map { responseState ->
            when (responseState) {
                is ResponseState.Loading -> {
                    ResponseState.Loading()
                }
                is ResponseState.Success -> {
                    ResponseState.Success(responseState.data!!.toEntity(), responseState.message)
                }
                is ResponseState.Error -> {
                    ResponseState.Error(null,responseState.message ?: "")
                }
            }
        }
    }


    override suspend fun login(loginParam: LoginParam): Flow<ResponseState<UserInfoEntity>> {
        return BaseRepository.callAPI {
            authDataSource.login(
                postData = loginParam,
            )
        }.map { responseState ->
            when (responseState) {
                is ResponseState.Loading -> {
                    ResponseState.Loading()
                }
                is ResponseState.Success -> {
                    ResponseState.Success(responseState.data!!.toEntity(), responseState.message)
                }
                else -> {
                    ResponseState.Error(null,responseState.message ?: "")
                }
            }
        }
    }

    override suspend fun refreshDeviceToken(deviceToken: String): Flow<ResponseState<RefreshDeviceTokenEntity>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["device_token"] = deviceToken

            authDataSource.refreshDeviceToken(
                postData = postData,
                accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2YjhjNjA4YjFmODUwNzU1NzcwMDg3ZCIsImlhdCI6MTcyMzM4ODAyMX0.X7bLhNUuRmNlhSP21ciiAwKLPBFTzsPT-GC_9uCqZbw"
            )
        }.map {
            when (it) {
                is ResponseState.Loading -> {
                    ResponseState.Loading()
                }

                is ResponseState.Success -> {
                    ResponseState.Success(it.data!!.toEntity(), it.message)
                }

                is ResponseState.Error -> {
                    ResponseState.Error(null, it.message ?: "")
                }
            }
        }
    }

    override suspend fun logout(): Flow<ResponseState<Boolean>> {
        return BaseRepository.callAPI {
            authDataSource.logout(
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }

    override suspend fun refreshToken(refreshToken: String): Flow<ResponseState<RefreshTokenEntity>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["refreshToken"] = refreshToken

            authDataSource.refreshToken(
                postData = postData,
            )
        }.map {
            when (it) {
                is ResponseState.Loading -> {
                    ResponseState.Loading()
                }

                is ResponseState.Success -> {
                    ResponseState.Success(it.data!!.toEntity(), it.message)
                }
                is ResponseState.Error -> {
                    ResponseState.Error(null, it.message ?: "")
                }
            }
        }
    }
}