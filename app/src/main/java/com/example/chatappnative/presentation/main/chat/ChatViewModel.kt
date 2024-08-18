package com.example.chatappnative.presentation.main.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.model.ChatModel
import com.example.chatappnative.data.socket.SocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject constructor(
    private val socketManager: SocketManager
) : ViewModel() {
    private val _selectedTabbarIndex = MutableStateFlow(0)
    val selectedTabbarIndex = _selectedTabbarIndex

    val tabs = listOf("Chats", "Calls")

    private val _isLoadingChatList = MutableStateFlow(false)
    val isLoadingChatList = _isLoadingChatList

    private val _chatList = MutableStateFlow(arrayOf<ChatModel>().toList())
    val chatList = _chatList

    private val _callList = MutableStateFlow(arrayOf<ChatModel>().toList())
    val callList = _callList

    private val _isLoadingCallList = MutableStateFlow(false)
    val isLoadingCallList = _isLoadingCallList

    var isRefreshing = MutableStateFlow(false)

    init {
//        Log.d("ChatViewModel", "socket connection: ${socketManager.socket?.connected()}")
        socketManager.connect()
        fetchData(all = true)
    }

    private fun fetchData(all: Boolean = false) {
        if (all) {
            getChatList()
            getCallList()
            return
        }

        if (_selectedTabbarIndex.value == 0) {
            getChatList()
        } else {
            getCallList()
        }
    }

    private fun getChatList() {
        viewModelScope.launch {
            try {
                _isLoadingChatList.value = true

                delay(2000L)

                _chatList.value = listOf(
                    ChatModel(
                        name = "John Doe",
                        type = "normal",
                        lastMessage = "Hello, how are you?",
                        typeMessage = "text",
                        createdDate = Date(),
                        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT26MP9f5YdlTfN-2pikGFAXSyfPfT7l-wdhA&s"
                    ),
                    ChatModel(
                        name = "Test 1",
                        type = "normal",
                        lastMessage = "",
                        typeMessage = "image",
                        createdDate = Date(),
                        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT26MP9f5YdlTfN-2pikGFAXSyfPfT7l-wdhA&s"
                    ),
                    ChatModel(
                        name = "Test 2",
                        type = "normal",
                        lastMessage = "",
                        typeMessage = "video",
                        createdDate = Date(),
                        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT26MP9f5YdlTfN-2pikGFAXSyfPfT7l-wdhA&s"
                    ),
                    ChatModel(
                        name = "Test 4",
                        type = "normal",
                        lastMessage = "",
                        typeMessage = "record",
                        createdDate = Date(),
                        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT26MP9f5YdlTfN-2pikGFAXSyfPfT7l-wdhA&s"
                    )
                )

                _isLoadingChatList.value = false
            } catch (e: Exception) {
                _isLoadingChatList.value = false
            }
        }
    }

    private fun getCallList() {
        viewModelScope.launch {
            try {
                _isLoadingCallList.value = true

                delay(2000L)

                _callList.value = listOf(
                    ChatModel(
                        name = "John Doe",
                        type = "normal",
                        lastMessage = "Hello, how are you?",
                        typeMessage = "text",
                        createdDate = Date(),
                        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT26MP9f5YdlTfN-2pikGFAXSyfPfT7l-wdhA&s"
                    ),
                    ChatModel(
                        name = "Test 1",
                        type = "normal",
                        lastMessage = "",
                        typeMessage = "image",
                        createdDate = Date(),
                        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT26MP9f5YdlTfN-2pikGFAXSyfPfT7l-wdhA&s"
                    ),
                    ChatModel(
                        name = "Test 2",
                        type = "normal",
                        lastMessage = "",
                        typeMessage = "video",
                        createdDate = Date(),
                        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT26MP9f5YdlTfN-2pikGFAXSyfPfT7l-wdhA&s"
                    ),
                    ChatModel(
                        name = "Test 4",
                        type = "normal",
                        lastMessage = "",
                        typeMessage = "record",
                        createdDate = Date(),
                        image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT26MP9f5YdlTfN-2pikGFAXSyfPfT7l-wdhA&s"
                    )
                )

                _isLoadingCallList.value = false
            } catch (e: Exception) {
                _isLoadingCallList.value = false
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            delay(1000)
            fetchData()
            isRefreshing.value = false
        }
    }

    fun onUpdateSelectedTabbarIndex(index: Int) {
        _selectedTabbarIndex.value = index
    }


    val onSubmitted: (String) -> Unit = {
        fetchData()
    }
}