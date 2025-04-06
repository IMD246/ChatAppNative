package com.example.chatappnative.presentation.main.contact.data.domain.entity

import com.example.chatappnative.presentation.main.contact.data.model.FriendModel
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID

data class FriendEntity(
    @SerializedName("id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("name") val name: String = "",
    @SerializedName("urlImage") val urlImage: String? = "",
    @SerializedName("presence") val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: Date,
) {
    fun toFriendModel() = FriendModel(
        id = id,
        name = name,
        urlImage = urlImage,
        presence = presence,
        presenceTimestamp = DateFormatUtil.formatDateToUtc(presenceTimestamp),
    )
}