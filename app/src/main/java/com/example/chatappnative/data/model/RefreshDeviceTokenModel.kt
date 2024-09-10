package com.example.chatappnative.data.model

import com.google.gson.annotations.SerializedName

data class RefreshDeviceTokenModel(
    @SerializedName("deviceToken") val deviceToken: String = "",
    @SerializedName("user_id") val userId: String = "",
)