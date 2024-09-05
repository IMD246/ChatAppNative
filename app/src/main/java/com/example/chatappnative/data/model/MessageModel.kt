package com.example.chatappnative.data.model

import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class MessageModel(
    @SerializedName("message") val message: String = "",
    val isMine: Boolean = false,
    @SerializedName("stampTimeMessage") val timeStamp: String = "",
    @SerializedName("messageStatus") val status: String = "not-sent",
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("typeMessage") val type: String = "text",
    val senderName: String = "",
    @SerializedName("avatar") val senderAvatar: String = "",
) {
    companion object {
        var count = 1
        fun getMessages(): List<MessageModel> {
            // Define the two different timestamps
            var year = 2022 - count
            val timestamp1 = "$year-01-01T10:00:00Z"
            val timestamp2 = "$year-01-01T15:00:00Z"


            // Generate 25 messages with timestamp1
            val messagesWithTimestamp1 = (1..5).map { index ->
                MessageModel(
                    message = "Message $index with Timestamp 1",
                    isMine = index % 2 == 0, // Alternate between true and false
                    timeStamp = timestamp1,
                    status = if (index % 5 == 0) "sent" else "not-sent",
                    type = "text",
                    senderName = if (index % 2 == 0) "John Doe" else "Me",
                    senderAvatar = "https://www.w3schools.com/howto/img_avatar.png"
                )
            }

            // Generate 25 messages with timestamp2
            val messagesWithTimestamp2 = (6..10).map { index ->
                MessageModel(
                    message = "Message $index with Timestamp 2",
                    isMine = index % 2 == 0, // Alternate between true and false
                    timeStamp = timestamp2,
                    status = if (index % 5 == 0) "sent" else "not-sent",
                    type = "text",
                    senderName = if (index % 2 == 0) "John Doe" else "Me",
                    senderAvatar = "https://www.w3schools.com/howto/img_avatar.png"
                )
            }

            count++
            // Combine both lists
            return messagesWithTimestamp1 + messagesWithTimestamp2
        }
    }

    fun getDateTimeMessage(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(timeStamp)

        return parseToUtc
    }
}