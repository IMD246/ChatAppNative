package com.example.chatappnative.domain.entity

data class RefreshTokenEntity(
    val accessExpiredTime: Long = 0,
    val accessToken: String = "",
    val refreshExpiredTime: Long = 0,
    val refreshToken: String = "",
)
{
    fun isExpired(): Boolean {
        return accessExpiredTime < System.currentTimeMillis()
    }

    fun isExpiredRefreshToken(): Boolean {
        return refreshExpiredTime < System.currentTimeMillis()
    }
}