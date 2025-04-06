package com.example.chatappnative.presentation.auth.data.domain.entity

import com.example.chatappnative.gateway.api.EntityMapper
import com.google.gson.annotations.SerializedName

data class RefreshTokenEntity(
    @SerializedName("accessExpiredTime") val accessExpiredTime: Long = 0,
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("refreshExpiredTime") val refreshExpiredTime: Long = 0,
    @SerializedName("refreshToken") val refreshToken: String = "",
) : EntityMapper<RefreshTokenEntity> {

    override fun toEntity(): RefreshTokenEntity {
        return RefreshTokenEntity(
            refreshToken = refreshToken,
            accessToken = accessToken,
            refreshExpiredTime = refreshExpiredTime,
            accessExpiredTime = accessExpiredTime,
            )
    }
}