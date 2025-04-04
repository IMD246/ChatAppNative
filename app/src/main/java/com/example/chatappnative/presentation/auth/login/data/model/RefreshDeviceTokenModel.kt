package com.example.chatappnative.gateway.model

import com.example.chatappnative.domain.entity.EntityMapper
import com.example.chatappnative.domain.entity.RefreshDeviceTokenEntity
import com.google.gson.annotations.SerializedName

data class RefreshDeviceTokenModel(
    @SerializedName("deviceToken") val deviceToken: String = "",
    @SerializedName("user_id") val userId: String = "",
) : EntityMapper<RefreshDeviceTokenEntity> {

    override fun toEntity(): RefreshDeviceTokenEntity {
        return RefreshDeviceTokenEntity(
            deviceToken = deviceToken,
            userId = userId,
            )
    }
}