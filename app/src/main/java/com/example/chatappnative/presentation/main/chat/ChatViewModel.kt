package com.example.chatappnative.presentation.main.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ChatViewModel : ViewModel() {
    private val _selectedTabbarIndex = MutableStateFlow(0)
    val selectedTabbarIndex = _selectedTabbarIndex

    fun onUpdateSelectedTabbarIndex(index: Int) {
        _selectedTabbarIndex.value = index
    }


    val onSummited: (String) -> Unit = {
    }
}