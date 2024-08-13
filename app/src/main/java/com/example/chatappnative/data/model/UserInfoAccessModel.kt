package com.example.chatappnative.data.model

data class UserInfoAccessModel(
    val accessToken: String,
    val email: String,
    val isDarkMode: Boolean,
    val name: String,
    val urlImage: String
)