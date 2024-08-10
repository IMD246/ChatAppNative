package com.example.chatappnative.data.data_source

import retrofit2.http.GET
import retrofit2.http.Query

interface ExampleDataSource {
    @GET("/api/v3/coins/markets?page")
    suspend fun getAllCoins(@Query("page") page: String)
}