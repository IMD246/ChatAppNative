package com.example.chatappnative.data.model

data class ContactModel(
    val name: String = "",
    val imageUrl: String? = "",
    val presence: Boolean = false,
)