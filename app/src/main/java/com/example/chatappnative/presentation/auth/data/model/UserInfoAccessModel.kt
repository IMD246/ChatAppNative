package com.example.chatappnative.presentation.auth.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.presentation.auth.data.domain.entity.UserInfoEntity
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName

data class UserInfoAccessModel(
    @SerializedName("accessToken") val accessToken: String = "",
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
            email = email,
            isDarkMode = isDarkMode,
            name = name,
            phone = phone,
            presenceTimeStamp = DateFormatUtil.parseToLocalDate(presenceTimeStamp),
            refreshToken = refreshToken,
            refreshTokenExpired = refreshTokenExpired,
            tokenExpired = tokenExpired,
            urlImage = urlImage,
        )
    }
}

