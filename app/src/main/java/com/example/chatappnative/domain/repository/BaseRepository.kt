package com.example.chatappnative.domain.repository

import com.example.chatappnative.data.BaseReponse
import com.example.chatappnative.data.ErrorReponse
import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.api.APIException
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.net.SocketException

object BaseRespository {
    suspend fun <T> callAPI(
        action: suspend () -> Response<BaseReponse<T>>,
    ): Flow<ResponseState<T>> = flow {
        try {
            emit(ResponseState.Loading())

            val response = action()

            val data = response.body()

            if (response.code() == 200) {
                emit(ResponseState.Success(data?.data, message = data?.message))
            } else {
                val dataError = response.errorBody()?.string()
                val errorResponse = Gson().fromJson(dataError, ErrorReponse::class.java)
                emit(ResponseState.Error(message = errorResponse.message))
            }

        } catch (e: Exception) {
            emit(ResponseState.Error(message = e.message ?: "Error occurred"))
        } catch (e: SocketException) {
            emit(ResponseState.Error(message = "Please check your network!"))
        } catch (e: APIException) {
            emit(ResponseState.Error(message = e.message))
        }
    }
}
