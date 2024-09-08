package com.example.chatappnative.service

import com.example.chatappnative.data.model.FriendModel
import com.example.chatappnative.data.model.FriendStatusModel
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.event.AddContactEvent
import com.example.chatappnative.event.AddFriendEvent
import com.example.chatappnative.event.NewMessageEvent
import com.example.chatappnative.event.UnauthorizedEvent
import com.example.chatappnative.event.UpdateSentMessageEvent
import com.example.chatappnative.event.UpdateUserPresenceEvent
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

    fun sendUpdateUserPresenceEvent(userPresenceSocketModel: UserPresenceSocketModel) {
        eventBus.post(UpdateUserPresenceEvent(userPresenceSocketModel))
    }

    fun sendUnauthorizedEvent(message: String = "Phiên đăng nhập của bạn đã hết hạn!") {
        eventBus.post(UnauthorizedEvent(message))
    }

    fun sendUpdateSentMessageEvent(updateSentMessageEvent: UpdateSentMessageEvent) {
        eventBus.post(updateSentMessageEvent)
    }

    fun sendNewMessageEvent(message: MessageModel) {
        eventBus.post(NewMessageEvent(message))
    }

    fun register(subscriber: Any) {
        eventBus.register(subscriber)
    }

    fun unregister(subscriber: Any) {
        eventBus.unregister(subscriber)
    }
}