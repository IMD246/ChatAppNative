package com.example.chatappnative.presentation.main.chat.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.presentation.auth.data.model.UserPresenceModel
import com.example.chatappnative.presentation.main.chat.data.domain.entity.ChatEntity
import com.example.chatappnative.presentation.main.chat.data.param.TypeChat
import com.example.chatappnative.presentation.main.chat.data.param.TypeMessage
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ChatModel(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("lastMessage") val lastMessage: String = "",
    @SerializedName("nameChat") val nameChat: String = "",
    @SerializedName("timeLastMessage") val timeLastMessage: String = "",
    @SerializedName("type") val type: String = "",
    @SerializedName("urlImage") val urlImage: String = "",
    @SerializedName("userIDLastMessage") val userIDLastMessage: String = "",
    @SerializedName("userNameLastMessage") val userNameLastMessage: String = "",
    @SerializedName("typeMessage") val typeMessage: String = "",
    @SerializedName("users") val usersPresence: List<UserPresenceModel> = arrayListOf(),
    @SerializedName("typeLastMessage") val typeLastMessage: String = "",
) : EntityMapper<ChatEntity> {

    override fun toEntity(): ChatEntity {
        return ChatEntity(
            id = id,
            lastMessage = lastMessage,
            nameChat = nameChat,
            timeLastMessage = DateFormatUtil.parseUtcToDate(timeLastMessage),
            type = TypeChat.fromType(type) ?: TypeChat.PERSONAL,
            urlImage = urlImage,
            userIDLastMessage = userIDLastMessage,
            userNameLastMessage = userNameLastMessage,
            usersPresence = usersPresence,
            typeLastMessage = TypeMessage.fromType(typeLastMessage) ?: TypeMessage.TEXT,
        )
    }
}