package com.example.chatappnative.presentation.main.contact.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.presentation.main.contact.data.domain.entity.ContactEntity
import com.example.chatappnative.util.DateFormatUtil
import com.google.gson.annotations.SerializedName
import java.util.UUID

data class ContactModel(
    @SerializedName("id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("name") val name: String = "",
    @SerializedName("status") val status: Int = 1,
    @SerializedName("urlImage") val urlImage: String = "",
    @SerializedName("presence") val presence: Boolean = false,
    @SerializedName("presence_timestamp") val presenceTimestamp: String = "",
) : EntityMapper<ContactEntity>
{
    override fun toEntity(): ContactEntity {
        return ContactEntity(
            id = id,
            name = name,
            status = status,
            urlImage = urlImage,
            presence = presence,
            presenceTimestamp = DateFormatUtil.parseUtcToDate(presenceTimestamp),
        )
    }
}