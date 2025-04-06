package com.example.chatappnative.presentation.auth.data.domain.entity

import com.google.gson.annotations.SerializedName

data class RefreshDeviceTokenEntity(
    @SerializedName("deviceToken") val deviceToken: String = "",
    @SerializedName("user_id") val userId: String = "",
)