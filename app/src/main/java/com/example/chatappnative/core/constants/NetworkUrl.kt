package com.example.chatappnative.core.constants

object NetworkUrl {
    const val BASE_URL = "https://chat-node-server-eight.vercel.app";

    //    const val BASE_URL = "http://localhost:5000";
    const val REGISTER = "/api/auth/register";
    const val LOGIN = "/api/auth/login";

    const val GET_CHAT_LIST = "/api/chat/get-chats";
    const val GET_FRIEND_LIST = "/api/friend/get-friend";
}