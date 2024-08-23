package com.example.chatappnative.service

import com.example.chatappnative.event.AddContactEvent
import org.greenrobot.eventbus.EventBus

object EventBusService {
    private val eventBus = EventBus.getDefault()

    fun sendEvent(typeEvent: String, payload: Any) {
        when (typeEvent) {
            "AddContactEvent" -> {
                sendAddContactEvent(payload)
            }
        }
    }

    private fun sendAddContactEvent(payload: Any) {
        eventBus.post(AddContactEvent(payload as String))
    }

    fun register(subscriber: Any) {
        eventBus.register(subscriber)
    }

    fun unregister(subscriber: Any) {
        eventBus.unregister(subscriber)
    }
}