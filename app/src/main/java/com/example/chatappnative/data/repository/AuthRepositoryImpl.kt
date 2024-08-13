package com.example.chatappnative.data.repository

import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.api.APIException
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
    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Flow<ResponseState<Boolean>> =
        flow {
            try {
                emit(ResponseState.Loading())

                val postData = HashMap<String, String>()

                postData["name"] = name
                postData["email"] = email
                postData["password"] = password

                val response = authDataSource.register(
                    postData
                )

                if (response.code == 200) {
                    emit(ResponseState.Success(true, message = response.message))
                } else {
                    emit(ResponseState.Error(message = response.message))
                }

            } catch (e: HttpException) {
                if (e.message?.contains("500") == true) {
                    emit(ResponseState.Error(message = "Internal Server Error"))
                } else {
                    emit(ResponseState.Error(message = "Error occurred"))
                }
            } catch (e: SocketException) {
                emit(ResponseState.Error(message = "Please check your network!"))
            } catch (e: APIException) {
                emit(ResponseState.Error(message = e.message))
            }
        }
}