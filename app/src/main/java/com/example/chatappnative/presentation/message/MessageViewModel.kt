package com.example.chatappnative.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.api.APIConstants
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.helper.DialogAPIHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel
@Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val _triggerScroll = MutableStateFlow(false)
    val triggerScroll = _triggerScroll

    private val pageSize = APIConstants.PAGE_SIZE

    private val _messageList = MutableStateFlow(arrayOf<MessageModel>().toList())
    val messageList = _messageList

    private var _messageText = MutableStateFlow("")
    val messageText = _messageText

    private var _isTyping = MutableStateFlow(false)
    val isTyping = _isTyping

    private val _isLoadingMessageList = MutableStateFlow(false)
    val isLoadingMessageList = _isLoadingMessageList


    private var _pagedMessageList = PagedListModel<MessageModel>()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private var _audioMode = MutableStateFlow(false)
    val audioMode = _audioMode

    private val _isMessageListLoadMore = MutableStateFlow(false)
    val isMessageListLoadMore = _isMessageListLoadMore

    init {
        viewModelScope.launch {
            fetchData()
        }
    }

    fun updateItemPresence(value: UserPresenceSocketModel) {
//        val contactListUpdated = contactList.value.toMutableList()
//
//        val item = contactListUpdated.find { it.id == value.userId } ?: return
//
//        val index = contactListUpdated.indexOf(item)
//
//        contactListUpdated[index] =
//            item.copy(
//                presence = value.presence,
//                presenceTimestamp = value.presenceTimestamp
//            )
//
//        _contactList.value = contactListUpdated
    }

    private suspend fun fetchData(
        clear: Boolean = false,
        isLoadMore: Boolean = false,
    ) {
        if (clear) {
            _messageList.value = listOf()
        }

        viewModelScope.launch {
            getMessageList(isLoadMore)
        }.join()
    }

    private suspend fun getMessageList(isLoadMore: Boolean = false) {
        viewModelScope.launch {
            if (!isLoadMore) {
                _isLoadingMessageList.value = true
            }
            val messageList = MessageModel.getMessages().plus(_messageList.value)
            delay(2000L)
            _messageList.value = messageList
            if (!isLoadMore) {
                _isLoadingMessageList.value = false
            }
//            contactRepository.getContactList(
//                page = _pagedContactList.currentPage + 1,
//                keyword = _keyword,
//                pageSize = pageSize,
//            ).collectLatest {
//                when (it) {
//                    is ResponseState.Error -> {
//                        if (!isLoadMore) {
//                            _isLoadingContactList.value = false
//                        }
//                    }
//
//                    is ResponseState.Loading -> {
//                        if (!isLoadMore) {
//                            _isLoadingContactList.value = true
//                        }
//                    }
//
//                    is ResponseState.Success -> {
//                        if (!isLoadMore) {
//                            _isLoadingContactList.value = false
//                        }
//                        val data = it.data ?: return@collectLatest
//
//                        _pagedContactList = data
//                        _contactList.value = _contactList.value.plus(data.data)
//                    }
//                }
//            }
        }.join()
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            fetchData(clear = true)
            _isRefreshing.value = false
        }
    }

    suspend fun loadMore() {
        if (_isLoadingMessageList.value) return

//        if (_pagedMessageList.currentPage >= _pagedMessageList.totalPages) return

        viewModelScope.launch {
            _isMessageListLoadMore.value = true
            fetchData(isLoadMore = true)
            _isMessageListLoadMore.value = false
        }
    }

    fun getUserInfo() = preferences.getUserInfo()

    val onChangedMessageText: (String) -> Unit = {
        _messageText.value = it
        onUpdateIsTyping()
    }

    private fun onUpdateIsTyping() {
        _isTyping.value = _messageText.value.isNotEmpty()
    }

    fun onAudio() {
        _audioMode.value = !_audioMode.value
    }

    fun onSend() {
        _messageList.value = _messageList.value.plus(
            MessageModel(
                message = _messageText.value,
                status = "not-sent",
                isMine = true,
            )
        )
        _messageText.value = ""
        _triggerScroll.value = true
    }

    fun onUpdateTriggerScroll(value: Boolean) {
        _triggerScroll.value = value
    }
}