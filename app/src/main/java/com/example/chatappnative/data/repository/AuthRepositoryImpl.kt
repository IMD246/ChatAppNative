package com.example.chatappnative.data.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.data_source.AuthDataSource
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.example.chatappnative.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<ResponseState<UserInfoAccessModel>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["name"] = name
            postData["email"] = email
            postData["password"] = password

            authDataSource.register(
                postData = postData
            )
        }
    }


    override suspend fun login(
        email: String,
        password: String
    ): Flow<ResponseState<UserInfoAccessModel>> {
        return BaseRepository.callAPI {
            val postData = HashMap<String, String>()

            postData["email"] = email
            postData["password"] = password

            authDataSource.login(
                postData = postData
            )
        }
    }
}