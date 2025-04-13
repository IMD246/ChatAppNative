package com.example.chatappnative.presentation.main.chat.data.domain.entity

import com.example.chatappnative.presentation.main.chat.data.param.TypeChat
import com.example.chatappnative.presentation.main.chat.data.param.TypeMessage
import com.example.chatappnative.presentation.auth.data.model.UserPresenceModel
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class ChatEntity(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("lastMessage") val lastMessage: String = "",
    @SerializedName("nameChat") val nameChat: String = "",
    @SerializedName("timeLastMessage") val timeLastMessage: Date,
    @SerializedName("type") val type: TypeChat = TypeChat.PERSONAL,
    @SerializedName("urlImage") val urlImage: String = "",
    @SerializedName("userIDLastMessage") val userIDLastMessage: String = "",
    @SerializedName("userNameLastMessage") val userNameLastMessage: String = "",
    @SerializedName("users") val usersPresence: List<UserPresenceModel> = arrayListOf(),
    @SerializedName("typeLastMessage") val typeLastMessage: TypeMessage = TypeMessage.TEXT,
) {
    fun getPresence(): Boolean {
        return usersPresence.any { it.presence }
    }

    fun getDateTimePresence(): Date {
        var getDate = ""

        if (usersPresence.isEmpty()) {
            return DateFormatUtil.parseUtcToDate(getDate)
        }

        var usersPresenceData = usersPresence

        usersPresenceData = usersPresenceData.sortedByDescending {
            it.presenceTimeStamp
        }

        val getFirstTimeStampPresence = usersPresenceData.first()

        getDate = getFirstTimeStampPresence.presenceTimeStamp

        val parseToUtc = DateFormatUtil.parseUtcToDate(getDate)

        return parseToUtc
    }
}