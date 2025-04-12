package com.example.chatappnative.presentation.main.chat.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.gateway.local_database.Preferences
import com.example.chatappnative.presentation.main.chat.data.domain.entity.GroupMessageEntity
import com.example.chatappnative.presentation.main.chat.data.domain.entity.MessageEntity
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class GroupMessageModel(
    @SerializedName("group-date") val groupDate: String = "",
    @SerializedName("messages") val messages: List<MessageModel> = emptyList(),
) : EntityMapper<GroupMessageEntity> {

    override fun toEntity(): GroupMessageEntity {
        return GroupMessageEntity(
            groupDate = groupDate,
            messages = messages.map { it.toEntity() },
        )
    }
}

data class MessageModel(
    @SerializedName("chatID") val chatID: String = "",
    @SerializedName("uuid") val uuid: String = UUID.randomUUID().toString(),
    @SerializedName("_id") val id: String = "",
    @SerializedName("userID") val senderId: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("stampTimeMessage") val timeStamp: String = "",
    @SerializedName("typeMessage") val typeMessage: String = "text",
    @SerializedName("messageStatus") val status: String = "not-sent",
    @SerializedName("avatar") val senderAvatar: String = "",
    @SerializedName("nameSender") val senderName: String = "",
) : EntityMapper<MessageEntity> {

    override fun toEntity(): MessageEntity {
        return MessageEntity(
            chatID = chatID,
            uuid = uuid,
            id = id,
            message = message,
            timeStamp = timeStamp,
            typeMessage = typeMessage,
            status = status,
            senderAvatar = senderAvatar,
            isMine = AppModel.userInfo?.userID == senderId,
            senderName = senderName,
            showAvatar = AppModel.userInfo?.userID != senderId,
            senderId = senderId,
        )
    }
}