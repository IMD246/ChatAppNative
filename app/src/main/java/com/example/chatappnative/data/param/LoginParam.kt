package com.example.chatappnative.data.param

import com.google.gson.annotations.SerializedName

data class LoginParam(
    @SerializedName("email") val email: String = "",
    @SerializedName("password") val password: String = "",
    @SerializedName("device_token") val deviceToken: String = "",
)