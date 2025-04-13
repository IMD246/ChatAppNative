package com.example.chatappnative.presentation.main.chat.data.domain.entity

import com.example.chatappnative.presentation.main.chat.data.model.MessageModel
import com.example.chatappnative.presentation.main.chat.data.param.StatusMessage
import com.example.chatappnative.presentation.main.chat.data.param.TypeMessage
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class GroupMessageEntity(
    @SerializedName("group-date") val groupDate: Date,
    @SerializedName("messages") val messages: List<MessageEntity> = emptyList(),
) {

    fun displayDateTime(): String {
        return DateFormatUtil.getFormattedDate(groupDate, "dd-MM-yyyy")
    }
}

data class MessageEntity(
    @SerializedName("chatID") val chatID: String = "",
    @SerializedName("uuid") val uuid: String = UUID.randomUUID().toString(),
    @SerializedName("_id") val id: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("stampTimeMessage") val timeStamp: Date,
    @SerializedName("typeMessage") val typeMessage: TypeMessage = TypeMessage.TEXT,
    @SerializedName("messageStatus") val status: StatusMessage = StatusMessage.NOT_SENT,
    @SerializedName("avatar") val senderAvatar: String = "",
    @SerializedName("isMine") val isMine: Boolean = false,
    @SerializedName("nameSender") val senderName: String = "",
    val showAvatar: Boolean = false,
    val senderId: String = UUID.randomUUID().toString(),
) {
    fun toModel(): MessageModel = MessageModel(
        chatID = chatID,
        uuid = uuid,
        id = id,
        message = message,
        timeStamp = DateFormatUtil.formatDateToIso8601(timeStamp),
        typeMessage = typeMessage.type,
        status = status.type,
        senderAvatar = senderAvatar,
        senderName = senderName,
        senderId = senderId,
    )

    fun displayDateTime(): String {
        return DateFormatUtil.getFormattedDate(timeStamp, "dd-MM-yyyy")
    }
}