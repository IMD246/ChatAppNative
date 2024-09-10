package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class UserInfoAccessModel(
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("deviceToken") val deviceToken: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("isDarkMode") val isDarkMode: Boolean = false,
    @SerializedName("name") val name: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("presence_timestamp") val presenceTimeStamp: String = "",
    @SerializedName("refreshToken") val refreshToken: String = "",
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

