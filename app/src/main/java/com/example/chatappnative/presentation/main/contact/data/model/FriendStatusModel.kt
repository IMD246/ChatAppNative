package com.example.chatappnative.presentation.main.contact.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.presentation.main.contact.data.domain.entity.FriendEntity
import com.example.chatappnative.presentation.main.contact.data.domain.entity.FriendStatusEntity
import com.google.gson.annotations.SerializedName

data class FriendStatusModel(
    @SerializedName("friend_id") val friendId: String,
    @SerializedName("sender_status") val senderStatus: Int,
    @SerializedName("friend_status") val friendStatus: Int,
    @SerializedName("friend_info") val friendInfo: FriendEntity,
    val urlImage: String = "",
) : EntityMapper<FriendStatusEntity>
{
    override fun toEntity(): FriendStatusEntity {
        return FriendStatusEntity(
            friendId = friendId,
            senderStatus = senderStatus,
            friendStatus = friendStatus,
            friendInfo = friendInfo,
            urlImage = urlImage
        )
    }
}