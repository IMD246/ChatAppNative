package com.example.chatappnative.data.model

import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date

data class ChatDetailModel(
    @SerializedName("_id") val id: String = "",
    val lastMessage: String = "",
    val nameChat: String = "",
    val timeLastMessage: String = "",
    val type: String = "",
    val urlImage: String = "",
    val userIDLastMessage: String = "",
    val userNameLastMessage: String = "",
    val typeMessage: String = "",
    @SerializedName("users") val usersPresence: List<UserPresence> = arrayListOf(),
    @SerializedName("messages") val messages: List<MessageModel> = arrayListOf(),
    @SerializedName("totalPages") val totalPages: Int = 0
) {
    fun getDateTimeLastMessage(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(timeLastMessage)

        return parseToUtc
    }

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