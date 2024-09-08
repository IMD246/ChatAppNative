package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class UserInfoAccessModel(
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("deviceToken") val deviceToken: String = "",
    val email: String = "",
    val isDarkMode: Boolean = false,
    val name: String = "",
    val phone: String = "",
    val presenceTimeStamp: String = "",
    val refreshToken: String = "",
    @SerializedName("refresh_token_expired") val refreshTokenExpired: Long,
    @SerializedName("token_expired") val tokenExpired: Long,
    @SerializedName("urlImage") val urlImage: String = "",
    @SerializedName("userID") val userID: String = ""
) {
    fun isExpired(): Boolean {
        return tokenExpired < System.currentTimeMillis()
    }

    fun isExpiredRefreshToken(): Boolean {
        return refreshTokenExpired < System.currentTimeMillis()
    }
}

