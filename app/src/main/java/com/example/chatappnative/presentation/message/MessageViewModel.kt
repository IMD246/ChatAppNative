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
import com.example.chatappnative.util.DateFormatUtil
import com.example.chatappnative.util.DateFormatUtil.DATE_TIME_FORMAT5
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
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val _triggerScroll = MutableStateFlow(false)
    val triggerScroll = _triggerScroll

    private val _messageList = MutableStateFlow(arrayOf<MessageModel>().toList())
    val messageList = _messageList

    private val _groupedByMessages: MutableStateFlow<List<Pair<String, List<MessageModel>>>> =
        MutableStateFlow(emptyList())
    val groupedByMessages = _groupedByMessages

    private var _messageText = MutableStateFlow("")
    val messageText = _messageText

    private var _isTyping = MutableStateFlow(false)
    val isTyping = _isTyping

    private val _isLoadingMessageList = MutableStateFlow(false)
    val isLoadingMessageList = _isLoadingMessageList

    private var _pagedMessageList =
        PagedListModel<MessageModel>(currentPage = 0, pageSize = APIConstants.PAGE_SIZE)

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
        }

        viewModelScope.launch {
            getChatDetail()
        }
    }

    private fun updateGroupedByMessages(
        messages: List<MessageModel>,
    ) {
        if (messages.isEmpty()) return

        _groupedByMessages.value = messages
            .groupBy { item ->
                item.timeStamp.substringBefore('T')
            }
            .toSortedMap(reverseOrder())
            .toList()

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
                pageSizeMessage = _pagedMessageList.pageSize,
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
                        _pagedMessageList = _pagedMessageList.copy(
                            currentPage = _pagedMessageList.currentPage + 1,
                            totalPages = data.totalPages,
                        )
                        updateGroupedByMessages(_messageList.value)
                    }
                }
            }
        }.join()
    }

    private suspend fun getMessageList(isLoadMore: Boolean = false) {
        viewModelScope.launch {
            chatRepository.getChatMessages(
                page = _pagedMessageList.currentPage + 1,
                pageSize = _pagedMessageList.pageSize,
                chatID = _chatDetailParam?.chatID ?: "",
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        if (!isLoadMore) {
                            _isLoadingMessageList.value = false
                        }
                    }

                    is ResponseState.Loading -> {
                        if (!isLoadMore) {
                            _isLoadingMessageList.value = true
                        }
                    }

                    is ResponseState.Success -> {
                        if (!isLoadMore) {
                            _isLoadingMessageList.value = false
                        }
                        val data = it.data ?: return@collectLatest

                        _pagedMessageList = data

                        val newMessages =
                            listOf<MessageModel>().plus(data.data + _messageList.value)

                        _messageList.value = newMessages

                        updateGroupedByMessages(_messageList.value)
                    }
                }
            }
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

        if (_isMessageListLoadMore.value) return

        if (_pagedMessageList.currentPage >= _pagedMessageList.totalPages) return

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
        val currentDateUtc0 = DateFormatUtil.getCurrentUtc0Date()
        val getFormatDate = DateFormatUtil.getFormattedUTCDate(
            currentDateUtc0,
            format = DATE_TIME_FORMAT5
        )

        val newMessage = MessageModel(
            message = _messageText.value,
            status = "not-sent",
            isMine = true,
            timeStamp = getFormatDate
        )

        _messageList.value = _messageList.value.plus(
            newMessage
        )

        _messageText.value = ""

        updateGroupedByMessages(_messageList.value)

        _triggerScroll.value = true
    }

    fun onUpdateTriggerScroll(value: Boolean) {
        _triggerScroll.value = value
    }
}