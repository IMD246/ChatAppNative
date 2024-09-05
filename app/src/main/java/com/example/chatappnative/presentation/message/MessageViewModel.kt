package com.example.chatappnative.presentation.message

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.api.APIConstants
import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.ChatDetailModel
import com.example.chatappnative.data.model.ChatDetailParamModel
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.domain.repository.ChatRepository
import com.example.chatappnative.helper.DialogAPIHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel
@Inject constructor(
    private val preferences: Preferences,
    private val chatRepository: ChatRepository,
    private val savedStateHandle: SavedStateHandle,
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

    private var _pagedMessageList = PagedListModel<MessageModel>(currentPage = 1)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private var _audioMode = MutableStateFlow(false)
    val audioMode = _audioMode

    private val _isMessageListLoadMore = MutableStateFlow(false)
    val isMessageListLoadMore = _isMessageListLoadMore

    private val _chatDetail = MutableStateFlow<ChatDetailModel?>(null)
    val chatDetail = _chatDetail

    private var _chatDetailParam: ChatDetailParamModel? = null

    private val messageError = Channel<String>()
    val messageErrorFlow = messageError.receiveAsFlow()

    init {
        savedStateHandle.get<ChatDetailParamModel>(MessageActivity.CHAT_PARAMS)?.let {
            _chatDetailParam = it

//            _chatDetailParam = ChatDetailParamModel(
//                listUserID = arrayListOf(
//                    "66c8c716cc3c12aa2e8107ff"
//                )
//            )
        }

        viewModelScope.launch {
            getChatDetail()
        }
    }

    fun updateItemPresence(value: UserPresenceSocketModel) {
        if (_chatDetail.value == null) return

        val chatDetailData = _chatDetail.value ?: return
        if (!chatDetailData.usersPresence.any { it.userID == value.userId }) return

        val newUsersPresence = chatDetailData.usersPresence.toMutableList()

        // continue if userPresence not found in userPresence list
        var findUserPresence = newUsersPresence.find { it.userID == value.userId } ?: return
        val getIndexUserPresence = newUsersPresence.indexOf(findUserPresence)

        findUserPresence = findUserPresence.copy(
            presence = value.presence,
            presenceTimeStamp = value.presenceTimestamp
        )

        newUsersPresence[getIndexUserPresence] = findUserPresence

        _chatDetail.value = chatDetailData.copy(
            usersPresence = newUsersPresence
        )
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

    private suspend fun getChatDetail() {
        viewModelScope.launch {
            chatRepository.getChatDetail(
                chatID = _chatDetailParam?.chatID,
                listUserID = _chatDetailParam?.listUserID,
                type = _chatDetailParam?.type ?: "personal",
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        messageError.send("Đã có lỗi xảy ra vui lòng thử lại!")
                        _isLoadingMessageList.value = false
                    }

                    is ResponseState.Loading -> {
                        _isLoadingMessageList.value = true
                    }

                    is ResponseState.Success -> {
                        _isLoadingMessageList.value = false
                        val data = it.data ?: return@collectLatest

                        _chatDetail.value = data
                        _messageList.value = data.messages
                    }
                }
            }
        }.join()
    }

    private suspend fun getMessageList(isLoadMore: Boolean = false) {
        viewModelScope.launch {
//            if (!isLoadMore) {
//                _isLoadingMessageList.value = true
//            }
//            val messageList = MessageModel.getMessages().plus(_messageList.value)
//            delay(2000L)
//            _messageList.value = messageList
//            if (!isLoadMore) {
//                _isLoadingMessageList.value = false
//            }
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
            if (_chatDetail.value == null) {
                getChatDetail()
            } else {
                fetchData(clear = true)
            }
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