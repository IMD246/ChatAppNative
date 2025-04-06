package com.example.chatappnative.gateway.api

interface EntityMapper<O> {
    fun toEntity(): O
}