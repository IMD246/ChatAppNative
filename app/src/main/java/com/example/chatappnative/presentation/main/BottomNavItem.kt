package com.example.chatappnative.presentation.main

import com.example.chatappnative.R

sealed class BottomNavItem(val route: String, val resourceId: Int, val label: String) {
    data object Chat : BottomNavItem("chat", R.drawable.ic_conversation, "Chats")
    data object Contact : BottomNavItem("contact", R.drawable.ic_contact, "Contacts")
    data object Setting : BottomNavItem("setting", R.drawable.ic_setting, "Setting")
}