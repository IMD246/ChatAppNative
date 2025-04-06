package com.example.chatappnative.presentation.main.contact.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.presentation.main.contact.data.domain.entity.UpdateFriendStatusEntity
import com.google.gson.annotations.SerializedName

data class UpdateFriendStatusModel(
    @SerializedName("user_status") val userStatus: Int = 0,
) : EntityMapper<UpdateFriendStatusEntity> {
    override fun toEntity(): UpdateFriendStatusEntity {
        return UpdateFriendStatusEntity(
            userStatus = userStatus
        )
    }
}