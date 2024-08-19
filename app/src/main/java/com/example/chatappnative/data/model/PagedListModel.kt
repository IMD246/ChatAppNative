package com.example.chatappnative.data.model

data class PagedListModel<T>(
    val data: List<T> = arrayListOf(),
    val currentPage: Int = 0,
    val pageSize: Int = 0,
    val total: Int = 0,
    val totalPages: Int = 0,
)