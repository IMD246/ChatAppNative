package com.example.chatappnative.data.repository

import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.data_source.AuthDataSource
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.RefreshDeviceTokenModel
import com.example.chatappnative.data.model.RefreshTokenModel
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.example.chatappnative.data.param.LoginParam
import com.example.chatappnative.data.param.RegisterParam
import com.example.chatappnative.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val preferences: Preferences,
) : AuthRepository {
    override suspend fun register(
        registerParam: RegisterParam,
    ): Flow<ResponseState<UserInfoAccessModel>> {
        return BaseRepository.callAPI {
            authDataSource.register(
                postData = registerParam
            )
        }
    }


    override suspend fun login(
        loginParam: LoginParam,
    ): Flow<ResponseState<UserInfoAccessModel>> {
        return BaseRepository.callAPI {
            authDataSource.login(
                postData = loginParam,
            )
        }
    }

    override suspend fun refreshDeviceToken(deviceToken: String): Flow<ResponseState<RefreshDeviceTokenModel>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["device_token"] = deviceToken

            authDataSource.refreshDeviceToken(
                postData = postData,
                accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2YjhjNjA4YjFmODUwNzU1NzcwMDg3ZCIsImlhdCI6MTcyMzM4ODAyMX0.X7bLhNUuRmNlhSP21ciiAwKLPBFTzsPT-GC_9uCqZbw"
            )
        }
    }

    override suspend fun logout(): Flow<ResponseState<Boolean>> {
        return BaseRepository.callAPI {
            authDataSource.logout(
                accessToken = "Bearer ${preferences.getAccessToken()}"
            )
        }
    }

    override suspend fun refreshToken(refreshToken: String): Flow<ResponseState<RefreshTokenModel>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["refreshToken"] = refreshToken

            authDataSource.refreshToken(
                postData = postData,
            )
        }
    }
}