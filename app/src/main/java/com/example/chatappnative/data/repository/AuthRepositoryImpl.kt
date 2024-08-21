package com.example.chatappnative.data.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.data_source.AuthDataSource
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.DeviceTokenModel
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.example.chatappnative.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val preferences: Preferences,
) : AuthRepository {
    override suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        deviceToken: String,
    ): Flow<ResponseState<UserInfoAccessModel>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["name"] = name
            postData["email"] = email
            postData["password"] = password
            postData["phone"] = phone
            postData["device_token"] = deviceToken
            authDataSource.register(
                postData = postData
            )
        }
    }


    override suspend fun login(
        email: String,
        password: String,
        deviceToken: String,
    ): Flow<ResponseState<UserInfoAccessModel>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["email"] = email
            postData["password"] = password
            postData["device_token"] = deviceToken
            authDataSource.login(
                postData = postData
            )
        }
    }

    override suspend fun refreshToken(deviceToken: String): Flow<ResponseState<DeviceTokenModel>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["device_token"] = deviceToken

            authDataSource.refreshDeviceToken(
                postData = postData,
//                accessToken = "Bearer ${preferences.getAccessToken()}"
                accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY2YjhjNjA4YjFmODUwNzU1NzcwMDg3ZCIsImlhdCI6MTcyMzM4ODAyMX0.X7bLhNUuRmNlhSP21ciiAwKLPBFTzsPT-GC_9uCqZbw"
            )
        }
    }
}