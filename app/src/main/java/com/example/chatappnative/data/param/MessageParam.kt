package com.example.chatappnative.data.param

enum class TypeMessage(val type: String) {
    TEXT("text"),
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio"),
}

enum class StatusMessage(val type: String) {
    TYPING("typing"),
    NOT_SENT("not-sent"),
    SENT("sent"),
    READ("read"),
    DELETE("delete")
}