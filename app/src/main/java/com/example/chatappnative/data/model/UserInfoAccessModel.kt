package com.example.chatappnative.data.model

data class UserInfoAccessModel(
    val accessToken: String = "",
    val email: String = "",
    val isDarkMode: Boolean = false,
    val name: String = "",
    val urlImage: String = "",
    val deviceTokenModel: String = "",
    val phone: String = "",
)