package com.example.chatappnative.event

import com.example.chatappnative.data.model.FriendModel

class AddFriendEvent(
    val status: Int,
    val friendModel: FriendModel
)