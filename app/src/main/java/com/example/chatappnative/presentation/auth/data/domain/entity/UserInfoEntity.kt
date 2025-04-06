package com.example.chatappnative.presentation.auth.data.domain.entity

data class UserInfoEntity(
    val accessToken: String = "",
    val deviceToken: String = "",
    val email: String = "",
    val isDarkMode: Boolean = false,
    val name: String = "",
    val phone: String = "",
    val presenceTimeStamp: String = "",
    val refreshToken: String = "",
    val refreshTokenExpired: Long = 0,
    val tokenExpired: Long = 0,
    val urlImage: String = "",
    val userID: String = ""
)
{
    fun isExpired(): Boolean {
        return tokenExpired < System.currentTimeMillis()
    }
    fun isExpiredRefreshToken(): Boolean {
        return refreshTokenExpired < System.currentTimeMillis()
    }
}