package com.example.chatappnative.data.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.data_source.AuthDataSource
import com.example.chatappnative.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.SocketException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {
    override suspend fun register(email: String, password: String): Flow<ResponseState<Boolean>> =
        flow {
            try {
                emit(ResponseState.Loading())

                val response = authDataSource.register(
                    email = email,
                    password = password
                )

                emit(ResponseState.Success(true))
            } catch (e: HttpException) {
                emit(ResponseState.Error(message = "Error occurred"))
            } catch (e: SocketException) {
                emit(ResponseState.Error(message = "Please check your network!"))
            }
        }
}