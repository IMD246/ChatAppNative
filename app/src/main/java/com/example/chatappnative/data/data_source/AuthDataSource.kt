package com.example.chatappnative.data.data_source

import com.example.chatappnative.core.constants.NetworkUrl.REGISTER
import com.example.chatappnative.data.BaseReponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthDataSource {
    @POST(REGISTER)
    suspend fun register(
        @Body() postData: Map<String, String>,
    ): BaseReponse
}