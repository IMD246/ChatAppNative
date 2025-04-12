package com.example.chatappnative.presentation.main.chat.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.presentation.auth.data.model.UserPresenceModel
import com.example.chatappnative.presentation.main.chat.data.domain.entity.ChatDetailEntity
import com.google.gson.annotations.SerializedName

data class ChatDetailModel(
    @SerializedName("_id") val id: String = "",
    @SerializedName("lastMessage") val lastMessage: String = "",
    @SerializedName("nameChat") val nameChat: String = "",
    @SerializedName("timeLastMessage") val timeLastMessage: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("urlImage") val urlImage: String = "",
    @SerializedName("userIDLastMessage") val userIDLastMessage: String = "",
    @SerializedName("userNameLastMessage") val userNameLastMessage: String = "",
    @SerializedName("typeMessage") val typeMessage: String = "",
    @SerializedName("users") val usersPresence: List<UserPresenceModel> = arrayListOf(),
    @SerializedName("messages") val messages: List<GroupMessageModel> = arrayListOf(),
    @SerializedName("totalPages") val totalPages: Int = 0
) : EntityMapper<ChatDetailEntity> {
    override fun toEntity(): ChatDetailEntity {
        return ChatDetailEntity(
            id = id,
            lastMessage = lastMessage,
            nameChat = nameChat,
            timeLastMessage = timeLastMessage,
            type = type,
            urlImage = urlImage,
            userIDLastMessage = userIDLastMessage,
            userNameLastMessage = userNameLastMessage,
            typeMessage = typeMessage,
            usersPresence = usersPresence,
            messages = messages.map { it.toEntity() },
            totalPages = totalPages
        )
    }
}