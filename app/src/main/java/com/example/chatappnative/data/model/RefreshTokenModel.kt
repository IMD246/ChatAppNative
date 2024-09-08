package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class RefreshTokenModel(
    @SerializedName("accessExpiredTime") val accessExpiredTime: Long = 0,
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("refreshExpiredTime") val refreshExpiredTime: Long = 0,
    @SerializedName("refreshToken") val refreshToken: String = "",
)