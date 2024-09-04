package com.example.chatappnative.data.model

import java.util.UUID

data class MessageModel(
    val message: String = "",
    val isMine: Boolean = false,
    val timeStamp: String = "",
    val status: String = "not-sent",
    val uuid: String = "",
    val id: String = UUID.randomUUID().toString(),
    val type: String = "text",
    val senderName: String = "",
    val senderAvatar: String = "",
) {
    companion object {
        fun getMessages(): List<MessageModel> {
            return listOf(
                MessageModel(
                    message = "Hello",
                    isMine = true,
                    status = "not-sent",
                ),
                MessageModel(
                    message = "asdadsasdjoajsdojioxjciozjxcojzxocjozxjciozxjcojzxocjasd;asdpasdmapsdmpasmdkoasmdoksamodkkmaosdmokasmd",
                    isMine = false,
                    status = "sent",
                ),
                MessageModel(
                    message = "Hello",
                    isMine = false,
                    status = "read",
                ),
                MessageModel(
                    message = "Hello",
                    isMine = true,
                    status = "not-sent",
                ),
                MessageModel(
                    message = "asdadsasdjoajsdojioxjciozjxcojzxocjozxjciozxjcojzxocjasd;asdpasdmapsdmpasmdkoasmdoksamodkkmaosdmokasmd",
                    isMine = false,
                    status = "sent",
                ),
                MessageModel(
                    message = "Hello",
                    isMine = false,
                    status = "read",
                ),
                MessageModel(
                    message = "asdadsasdjoajsdojioxjciozjxcojzxocjozxjciozxjcojzxocjasd;asdpasdmapsdmpasmdkoasmdoksamodkkmaosdmokasmd",
                    isMine = false,
                    status = "typing",
                ),
                MessageModel(
                    message = "Hello",
                    isMine = true,
                    status = "not-sent",
                ),
                MessageModel(
                    message = "asdadsasdjoajsdojioxjciozjxcojzxocjozxjciozxjcojzxocjasd;asdpasdmapsdmpasmdkoasmdoksamodkkmaosdmokasmd",
                    isMine = false,
                    status = "sent",
                ),
                MessageModel(
                    message = "Hello",
                    isMine = false,
                    status = "read",
                ),
                MessageModel(
                    message = "asdadsasdjoajsdojioxjciozjxcojzxocjozxjciozxjcojzxocjasd;asdpasdmapsdmpasmdkoasmdoksamodkkmaosdmokasmd",
                    isMine = false,
                    status = "typing",
                ),
            )
        }
    }
}