package com.example.chatappnative.event

import com.example.chatappnative.gateway.model.FriendModel

class AddFriendEvent(
    val status: Int,
    val friendModel: FriendModel
)