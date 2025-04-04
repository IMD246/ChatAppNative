package com.example.chatappnative.gateway.model

import com.example.chatappnative.domain.entity.EntityMapper
import com.example.chatappnative.domain.entity.RefreshTokenEntity
import com.google.gson.annotations.SerializedName

data class RefreshTokenModel(
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