package com.example.chatappnative.data.model

import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class ChatModel(
    @SerializedName("_id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("lastMessage") val lastMessage: String = "",
    @SerializedName("nameChat") val nameChat: String = "",
    @SerializedName("timeLastMessage") val timeLastMessage: String = "",
    val type: String = "",
    val urlImage: String = "",
    val userIDLastMessage: String = "",
    @SerializedName("userNameLastMessage") val userNameLastMessage: String = "",
    @SerializedName("typeMessage") val typeMessage: String = "",
    @SerializedName("users") val usersPresence: List<UserPresence> = arrayListOf(),
    @SerializedName("typeLastMessage") val typeLastMessage: String = "",
) {
    fun getDateTimeLastMessage(): Date {
        val parseToUtc = DateFormatUtil.parseUtcToDate(timeLastMessage)

        return parseToUtc
    }

    fun getPresence(): Boolean {
        return usersPresence.any { it.presence }
    }

    fun getDateTimePresence(): Date {
        return Date()
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