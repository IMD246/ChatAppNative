package com.example.chatappnative.data.param

import com.google.gson.annotations.SerializedName

data class RegisterParam(
    @SerializedName("name") val name: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("password") val password: String = "",
    @SerializedName("device_token") val deviceToken: String = "",
)