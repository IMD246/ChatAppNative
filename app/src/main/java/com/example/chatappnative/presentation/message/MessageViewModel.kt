package com.example.chatappnative.presentation.message

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.api.APIConstants
import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.ChatDetailModel
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.data.param.ChatDetailParam
import com.example.chatappnative.data.param.StatusMessage
import com.example.chatappnative.data.param.TypeMessage
import com.example.chatappnative.data.param.UserTypingParam
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.ChatRepository
import com.example.chatappnative.helper.DialogAPIHelper
import com.example.chatappnative.util.DateFormatUtil
import com.example.chatappnative.util.DateFormatUtil.DATE_FORMAT
import com.example.chatappnative.util.DateFormatUtil.DATE_TIME_FORMAT5
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("LABEL_NAME_CLASH")
@HiltViewModel
class MessageViewModel
@Inject constructor(
    private val preferences: Preferences,
    private val chatRepository: ChatRepository,
    savedStateHandle: SavedStateHandle,
    private val socketManager: SocketManager,
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val _triggerScroll = MutableStateFlow(false)
    val triggerScroll = _triggerScroll

    private val _messageList = MutableStateFlow(arrayOf<MessageModel>().toList())

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

    private var _chatDetailParam: ChatDetailParam? = null

    private val messageError = Channel<String>()
    val messageErrorFlow = messageError.receiveAsFlow()

    private val newMessage: MutableStateFlow<MessageModel?> = MutableStateFlow(null)
    val newMessageFlow = newMessage

    private var sendTypingMessage: Boolean = false

    init {
        savedStateHandle.get<ChatDetailParam>(MessageActivity.CHAT_PARAMS)?.let { params ->
            _chatDetailParam = params
        }

        viewModelScope.launch {
            getChatDetail()
        }

        onNewMessage()

        onUpdateSentMessage()

        onUserReadMessages()

        onUserTyping()

        onUserStopTyping()
    }

    private fun onUserReadMessages() {
        viewModelScope.launch {
            socketManager.onUserReadMessages {
                if (it != _chatDetail.value?.id) return@onUserReadMessages

                updateSentToReadMessages(false)
            }
        }
    }

    private fun updateSentToReadMessages(isEmit: Boolean = false) {
        if (_messageList.value.any { item -> item.status == StatusMessage.SENT.type && item.isMine }) {
            _messageList.value = _messageList.value.map { message ->
                if (message.status == StatusMessage.SENT.type && message.isMine) {
                    message.copy(status = StatusMessage.READ.type)
                } else {
                    message
                }
            }
            updateGroupedByMessages()

            if (isEmit) {
                socketManager.emitUpdateReadMessages(_chatDetail.value?.id ?: "")
            }
        }
    }

    private fun onUpdateSentMessage() {
        viewModelScope.launch {
            socketManager.onUpdateSentMessages {
                updateStatusMessage(
                    idMessage = it.idMessage,
                    uuid = it.uuid,
                    statusMessage = it.statusMessage,
                )
            }
        }
    }

    private fun onNewMessage() {
        viewModelScope.launch {
            socketManager.onNewMessage {
                if (it.chatID != _chatDetail.value?.id) return@onNewMessage

                val newList = mutableListOf(
                    it.copy(status = StatusMessage.READ.type)
                ).plus(
                    _messageList.value
                )

                _messageList.value = newList

                socketManager.emitUpdateReadMessages(_chatDetail.value?.id ?: "")

                updateGroupedByMessages()

                newMessage.value = it
            }
        }
    }

    private fun updateGroupedByMessages() {
        if (_messageList.value.isEmpty()) return

        _groupedByMessages.value = _messageList.value.asReversed()
            .groupBy { item ->
                val localDate = DateFormatUtil.parseUtcToDate(item.timeStamp)
                DateFormatUtil.getFormattedDate(localDate, DATE_FORMAT)
            }
            .toSortedMap(reverseOrder())
            .mapValues {
                val newMessages = it.value.toMutableList()

                var currentLastIndex = it.value.lastIndex

                val lastMessage = it.value[currentLastIndex]

                if (!lastMessage.isMine) {
                    newMessages[currentLastIndex] = lastMessage.copy(showAvatar = true)

                    currentLastIndex--
                }

                while (currentLastIndex > 0) {
                    val currentLastMessage = it.value[currentLastIndex]
                    val previousMessage = it.value[currentLastIndex - 1]

                    if (!previousMessage.isMine) {
                        if (currentLastMessage.isMine) {
                            newMessages[currentLastIndex - 1] =
                                previousMessage.copy(showAvatar = true)
                        }
                    }

                    currentLastIndex--
                }

                return@mapValues newMessages
            }
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
                        socketManager.joinRoom(_chatDetail.value?.id ?: "")
                        _messageList.value = data.messages
                        _pagedMessageList = _pagedMessageList.copy(
                            currentPage = _pagedMessageList.currentPage + 1,
                            totalPages = data.totalPages,
                        )
                        onEmitReadMessages()
                        updateGroupedByMessages()
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
                chatID = _chatDetail.value?.id ?: "",
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
                            listOf<MessageModel>().plus(_messageList.value + data.data)

                        _messageList.value = newMessages

                        updateGroupedByMessages()
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
        _isTyping.value = it.isNotEmpty()
        onSendTypingMessage()
    }

    private fun onSendTypingMessage() {
        if (_chatDetail.value == null) return

        if (!_isTyping.value) {
            if (!sendTypingMessage) return

            socketManager.emitUserStopTyping(
                UserTypingParam(
                    chatID = _chatDetail.value?.id ?: "",
                    userID = getUserInfo()?.userID ?: "",
                    senderAvatar = getUserInfo()?.urlImage ?: "",
                    senderName = getUserInfo()?.name ?: "",
                )
            )
            sendTypingMessage = false
        }

        if (isTyping.value) {
            if (sendTypingMessage) {
                return
            }

            socketManager.emitUserTyping(
                UserTypingParam(
                    chatID = _chatDetail.value?.id ?: "",
                    userID = getUserInfo()?.userID ?: "",
                    senderAvatar = getUserInfo()?.urlImage ?: "",
                    senderName = getUserInfo()?.name ?: "",
                )
            )

            sendTypingMessage = true
        }
    }

    fun onAudio() {
        _audioMode.value = !_audioMode.value
    }

    private fun updateStatusMessage(
        idMessage: String = "",
        uuid: String = "",
        statusMessage: String = "sent"
    ) {

        val newMessages = _messageList.value.toMutableList()

        var findMessage = newMessages.find { it.id == idMessage }

        if (findMessage == null) {
            findMessage = newMessages.find { it.uuid == uuid }
        }

        if (findMessage == null) return

        val index = newMessages.indexOf(findMessage)

        newMessages[index] = newMessages[index].copy(status = statusMessage)

        _messageList.value = newMessages

        updateGroupedByMessages()
    }

    fun onSend() {
        if (_isLoadingMessageList.value) return

        socketManager.emitUserStopTyping(
            UserTypingParam(
                chatID = _chatDetail.value?.id ?: "",
                userID = getUserInfo()?.userID ?: "",
                senderAvatar = getUserInfo()?.urlImage ?: "",
                senderName = getUserInfo()?.name ?: "",
            )
        )

        val currentDateUtc0 = DateFormatUtil.getCurrentUtc0Date()
        val getFormatDate = DateFormatUtil.getFormattedUTCDate(
            currentDateUtc0,
            format = DATE_TIME_FORMAT5
        )

        val userInfo = preferences.getUserInfo()

        val newMessage = MessageModel(
            message = _messageText.value,
            status = "not-sent",
            isMine = true,
            timeStamp = getFormatDate,
            typeMessage = TypeMessage.TEXT.type,
            senderName = userInfo?.name ?: "",
            senderAvatar = userInfo?.urlImage ?: "",
        )

        socketManager.emitClientSendMessage(
            message = newMessage,
            chatID = _chatDetail.value?.id ?: "",
            userId = userInfo?.userID ?: "",
        )
        val newList = mutableListOf(
            newMessage
        ).plus(
            _messageList.value
        )

        _messageList.value = newList

        _messageText.value = ""
        onChangedMessageText("")

        updateGroupedByMessages()

        _triggerScroll.value = true
    }

    fun onUpdateTriggerScroll(value: Boolean) {
        _triggerScroll.value = value
    }

    fun onLeaveRoom() {
        if (_chatDetail.value == null) return
        if (_isTyping.value) {
            socketManager.emitUserStopTyping(
                UserTypingParam(
                    chatID = _chatDetail.value?.id ?: "",
                    userID = getUserInfo()?.userID ?: "",
                    senderAvatar = getUserInfo()?.urlImage ?: "",
                )
            )
        }
        socketManager.leaveRoom(_chatDetail.value?.id ?: "")
    }

    private fun onEmitReadMessages() {
        if (_messageList.value.isEmpty()) return

        updateSentToReadMessages(isEmit = true)
    }

    fun clearNewMessage() {
        if (newMessage.value == null) return
        newMessage.value = null
    }

    private fun onUserTyping() {
        viewModelScope.launch {
            socketManager.onUserTyping { param ->
                if (param.chatID != _chatDetail.value?.id) return@onUserTyping

                val findTypingMessage = _messageList.value.find {
                    it.status == StatusMessage.TYPING.type && !it.isMine && it.senderId == param.userID
                }

                if (findTypingMessage != null) {
                    return@onUserTyping
                }

                val currentDateUtc0 = DateFormatUtil.getCurrentUtc0Date()
                val getFormatDate = DateFormatUtil.getFormattedUTCDate(
                    currentDateUtc0,
                    format = DATE_TIME_FORMAT5
                )

                val newMessage = MessageModel(
                    status = StatusMessage.TYPING.type,
                    isMine = false,
                    typeMessage = TypeMessage.TEXT.type,
                    timeStamp = getFormatDate,
                    senderName = param.senderName,
                    senderAvatar = param.senderAvatar,
                    senderId = param.userID,
                    chatID = param.chatID,
                )

                val newList = mutableListOf(
                    newMessage
                ).plus(
                    _messageList.value
                )

                _messageList.value = newList

                updateGroupedByMessages()
            }
        }
    }

    private fun onUserStopTyping() {
        socketManager.onUserStopTyping { param ->
            if (param.chatID != _chatDetail.value?.id) return@onUserStopTyping

            val newMessages = _messageList.value.toMutableList()

            val result =
                newMessages.removeIf { it.status == StatusMessage.TYPING.type && !it.isMine && it.senderId == param.userID }

            if (result) {
                _messageList.value = newMessages
                updateGroupedByMessages()
            }
        }
    }
}