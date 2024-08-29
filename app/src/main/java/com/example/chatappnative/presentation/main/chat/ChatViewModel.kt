package com.example.chatappnative.presentation.main.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.api.APIConstants
import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.ChatModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
@Inject constructor(
    private val socketManager: SocketManager,
    private val chatRepository: ChatRepository,
    private val preferences: Preferences,
) : ViewModel() {
    private val pageSize = APIConstants.PAGE_SIZE

    private val _selectedTabbarIndex = MutableStateFlow(0)
    val selectedTabbarIndex = _selectedTabbarIndex

    val tabs = listOf("Chats", "Calls")

    private val _isLoadingChatList = MutableStateFlow(false)
    val isLoadingChatList = _isLoadingChatList

    private var _pagedChatList = PagedListModel<ChatModel>()

    private val _chatList = MutableStateFlow(arrayOf<ChatModel>().toList())
    val chatList = _chatList

    private val _callList = MutableStateFlow(arrayOf<ChatModel>().toList())
    val callList = _callList

    private var _keyword: String? = null

    private val _isLoadingCallList = MutableStateFlow(false)
    val isLoadingCallList = _isLoadingCallList

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _isChatListLoadMore = MutableStateFlow(false)
    var isChatListLoadMore = _isChatListLoadMore

    init {
        viewModelScope.launch {
            fetchData(all = true)
        }
    }

    private suspend fun fetchData(
        all: Boolean = false,
        clear: Boolean = false,
        isLoadMore: Boolean = false,
    ) {
        if (all) {
            getChatList()
            getCallList()
            return
        }

        if (_selectedTabbarIndex.value == 0) {
            if (clear) {
                _chatList.value = listOf()
                _pagedChatList = PagedListModel()
            }

            viewModelScope.launch {
                getChatList(isLoadMore)
            }.join()

        } else {
            if (clear) {
                _callList.value = listOf()
            }
            getCallList()
        }
    }

    private suspend fun getChatList(isLoadMore: Boolean = false) {
        viewModelScope.launch {
            chatRepository.getChatList(
                page = _pagedChatList.currentPage + 1,
                keyword = _keyword,
                pageSize = pageSize,
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        if (!isLoadMore) {
                            _isLoadingChatList.value = false
                        }
                    }

                    is ResponseState.Loading -> {
                        if (!isLoadMore) {
                            _isLoadingChatList.value = true
                        }
                    }

                    is ResponseState.Success -> {
                        if (!isLoadMore) {
                            _isLoadingChatList.value = false
                        }
                        val data = it.data ?: return@collectLatest

                        _pagedChatList = data
                        _chatList.value = _chatList.value.plus(data.data)
                    }

                }
            }
        }.join()
    }

    private fun getCallList() {
        viewModelScope.launch {
            try {
                _isLoadingCallList.value = true

                delay(2000L)

                _callList.value = listOf(

                )

                _isLoadingCallList.value = false
            } catch (e: Exception) {
                _isLoadingCallList.value = false
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            fetchData(clear = true)
            _isRefreshing.value = false
        }
    }

    fun onUpdateSelectedTabbarIndex(index: Int) {
        _selectedTabbarIndex.value = index
    }


    val onSubmitted: (String) -> Unit = {
        _keyword = it.ifBlank {
            null
        }

        viewModelScope.launch {
            fetchData(clear = true)
        }
    }

    suspend fun loadMore() {
        if (_isChatListLoadMore.value) return

        viewModelScope.launch {
            _isChatListLoadMore.value = true
            fetchData(isLoadMore = true)
            _isChatListLoadMore.value = false
        }
    }

    fun updateItemPresence(value: UserPresenceSocketModel) {
        Log.d("ChatViewModel", "updateItemPresence: $value")

        val chatList = _chatList.value
        val chatListUpdated = _chatList.value.toMutableList()

        for (item in chatList) {
            if (!item.users.contains(value.userId)) {
                continue
            }

            val index = chatList.indexOf(item)
            chatListUpdated[index] =
                item.copy(presence = value.presence, presenceTimestamp = value.presenceTimestamp)
        }
        _chatList.value = chatListUpdated
    }

    fun getUserInfo(): UserInfoAccessModel? {
        return preferences.getUserInfo()
    }
}