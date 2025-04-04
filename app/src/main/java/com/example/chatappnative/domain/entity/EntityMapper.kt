package com.example.chatappnative.domain.entity

interface EntityMapper<O> {
    fun toEntity(): O
}