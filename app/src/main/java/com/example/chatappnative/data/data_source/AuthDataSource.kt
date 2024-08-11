package com.example.chatappnative.data.data_source

import retrofit2.http.GET
import retrofit2.http.Query

interface AuthDataSource {
    @GET("/api/v3/coins/markets?page")
    suspend fun register(@Query("email") email: String, @Query("password") password: String)
}