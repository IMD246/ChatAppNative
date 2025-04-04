package com.example.chatappnative.event

import com.example.chatappnative.gateway.model.UserPresenceSocketModel

class UpdateUserPresenceEvent(
    val userPresenceSocketModel: UserPresenceSocketModel,
)