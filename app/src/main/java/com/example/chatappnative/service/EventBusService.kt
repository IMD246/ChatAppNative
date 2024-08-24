package com.example.chatappnative.service

import com.example.chatappnative.data.model.FriendModel
import com.example.chatappnative.data.model.FriendStatusModel
import com.example.chatappnative.event.AddContactEvent
import com.example.chatappnative.event.AddFriendEvent
import org.greenrobot.eventbus.EventBus

object EventBusService {
    private val eventBus = EventBus.getDefault()

    fun sendEvent(typeEvent: String, payload: Any) {
        when (typeEvent) {
            "add_contact" -> {
                sendAddContactEvent(payload as FriendStatusModel)
            }
        }
    }

    private fun sendAddContactEvent(data: FriendStatusModel) {
        eventBus.post(AddContactEvent(data))
    }

    fun sendFriendEvent(status: Int, data: FriendModel) {
        eventBus.post(AddFriendEvent(status, data))
    }

    fun register(subscriber: Any) {
        eventBus.register(subscriber)
    }

    fun unregister(subscriber: Any) {
        eventBus.unregister(subscriber)
    }
}