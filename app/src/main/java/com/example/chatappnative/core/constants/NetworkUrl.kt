package com.example.chatappnative.core.constants

object NetworkUrl {
    const val BASE_URL = "https://chatnodeserver.onrender.com"

//    const val BASE_URL = "http://192.168.1.4:5000";

    //Auth
    const val REGISTER = "/api/auth/register"
    const val LOGIN = "/api/auth/login"
    const val REFRESH_DEVICE_TOKEN = "/api/auth/refresh-device-token"
    const val LOGOUT = "/api/auth/logout"
    const val REFRESH_TOKEN = "/api/auth/refresh-token"

    //Contact
    const val GET_FRIEND_LIST = "/api/friend/get-friend"
    const val GET_USER_LIST = "/api/user/get-users"
    const val UPDATE_FRIEND_STATUS = "/api/friend/update-friend-status"


    //Chat
    const val GET_CHAT_LIST = "/api/chat/get-chats"
    const val TAKE_ROOM_CHAT = "/api/chat/take-room-chat"

    //Message
    const val TAKE_MESSAGE_LIST = "/api/message/takeMessages"

}