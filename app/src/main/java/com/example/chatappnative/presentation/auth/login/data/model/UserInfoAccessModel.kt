package com.example.chatappnative.gateway.model

import com.example.chatappnative.domain.entity.EntityMapper
import com.example.chatappnative.domain.entity.UserInfoEntity
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
): EntityMapper<UserInfoEntity> {
    override fun toEntity(): UserInfoEntity {
        return UserInfoEntity(
            userID = userID,
            accessToken = accessToken,
            deviceToken = deviceToken,
            email = email,
            isDarkMode = isDarkMode,
            name = name,
            phone = phone,
            presenceTimeStamp = presenceTimeStamp,
            refreshToken = refreshToken,
            refreshTokenExpired = refreshTokenExpired,
            tokenExpired = tokenExpired,
            urlImage = urlImage,
        )
    }
}

