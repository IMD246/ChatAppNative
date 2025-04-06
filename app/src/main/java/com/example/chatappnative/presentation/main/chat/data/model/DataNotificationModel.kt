package com.example.chatappnative.presentation.main.chat.data.model

import com.example.chatappnative.gateway.api.EntityMapper
import com.example.chatappnative.presentation.main.chat.data.domain.entity.DataNotificationEntity

data class DataNotificationModel(
    val event: String = "",
    val data: Any
): EntityMapper<DataNotificationEntity> {
    override fun toEntity(): DataNotificationEntity {
        return DataNotificationEntity(event, data)
    }
}