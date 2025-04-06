package com.example.chatappnative.event

import com.example.chatappnative.presentation.main.contact.data.model.FriendModel

class AddFriendEvent(
    val status: Int,
    val friendModel: FriendModel
)