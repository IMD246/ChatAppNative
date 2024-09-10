package com.example.chatappnative.data.model

import com.example.chatappnative.data.param.StatusMessage
import com.example.chatappnative.data.param.TypeMessage
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class MessageModel(
    @SerializedName("chatID") val chatID: String = "",
    @SerializedName("uuid") val uuid: String = UUID.randomUUID().toString(),
    @SerializedName("_id") val id: String = "",
    @SerializedName("message") val message: String = "",
    @SerializedName("stampTimeMessage") val timeStamp: String = "",
    @SerializedName("typeMessage") val typeMessage: String = "text",
    @SerializedName("messageStatus") val status: String = "not-sent",
    @SerializedName("avatar") val senderAvatar: String = "",
    @SerializedName("isMine") val isMine: Boolean = false,
    @SerializedName("nameSender") val senderName: String = "",
    val showAvatar: Boolean = false,
    val senderId: String = UUID.randomUUID().toString(),
) {
    fun getDateTimeMessage(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(timeStamp)

        return parseToUtc
    }

    fun getTypeMessage(): TypeMessage {
        return when (typeMessage) {
            "image" -> TypeMessage.IMAGE
            "video" -> TypeMessage.VIDEO
            "audio" -> TypeMessage.AUDIO
            else -> {
                TypeMessage.TEXT
            }
        }
    }

    fun getStatusMessage(): StatusMessage {
        return when (status) {
            "typing" -> StatusMessage.TYPING
            "sent" -> StatusMessage.SENT
            "read" -> StatusMessage.READ
            "delete" -> StatusMessage.DELETE
            else -> {
                StatusMessage.NOT_SENT
            }
        }
    }
}