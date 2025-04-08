package com.example.chatappnative.presentation.auth.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.presentation.auth.data.domain.entity.UserInfoEntity
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName

data class UserInfoAccessModel(
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("refreshToken") val refreshToken: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("isDarkMode") val isDarkMode: Boolean = false,
    @SerializedName("urlImage") val urlImage: String = "",
    @SerializedName("presenceTimeStamp") val presenceTimeStamp: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("userID") val userID: String = "",
    @SerializedName("token_expired") val tokenExpired: Long,
    @SerializedName("refresh_token_expired") val refreshTokenExpired: Long,
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

