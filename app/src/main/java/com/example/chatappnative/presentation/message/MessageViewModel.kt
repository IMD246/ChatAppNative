package com.example.chatappnative.presentation.message

import androidx.lifecycle.ViewModel
import com.example.chatappnative.data.api.APIConstants
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.MessageModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.helper.DialogAPIHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MessageViewModel
@Inject constructor(
    private val preferences: Preferences,
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val pageSize = APIConstants.PAGE_SIZE

    private val _messageList = MutableStateFlow(arrayOf<MessageModel>().toList())
    val messageList = _messageList

    private var _messageText = MutableStateFlow("")
    val messageText = _messageText

    //    private val _isLoadingContactList = MutableStateFlow(false)
//    val isLoadingContactList = _isLoadingContactList
//
//    private var _pagedContactList = PagedListModel<ContactModel>()
//
//    private val _contactList = MutableStateFlow(arrayOf<ContactModel>().toList())
//    val contactList = _contactList
//
//    private var _keyword: String? = null
//
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private var _audioMode = MutableStateFlow(false)
    val audioMode = _audioMode
//
//    private val _isContactListLoadMore = MutableStateFlow(false)
//    var isContactListLoadMore = _isContactListLoadMore

    init {
        _messageList.value = MessageModel.getMessages()

//        viewModelScope.launch {
//            fetchData()
//        }
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

//    private suspend fun fetchData(
//        clear: Boolean = false,
//        isLoadMore: Boolean = false,
//    ) {
//        if (clear) {
//            _contactList.value = listOf()
//            _pagedContactList = PagedListModel()
//        }
//
//        viewModelScope.launch {
//            getContactList(isLoadMore)
//        }.join()
//    }

//    private suspend fun getContactList(isLoadMore: Boolean = false) {
//        viewModelScope.launch {
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
//        }.join()
//    }

//    fun onRefresh() {
//        viewModelScope.launch {
//            _isRefreshing.value = true
//            fetchData(clear = true)
//            _isRefreshing.value = false
//        }
//    }

//    suspend fun loadMore() {
//        if (_isContactListLoadMore.value) return
//
//        if (_pagedContactList.currentPage >= _pagedContactList.totalPages) return
//
//        viewModelScope.launch {
//            _isContactListLoadMore.value = true
//            fetchData(isLoadMore = true)
//            _isContactListLoadMore.value = false
//        }
//    }

    fun getUserInfo() = preferences.getUserInfo()

    val onChangedMessageText: (String) -> Unit = {
        _messageText.value = it
    }

    fun onAudio() {
        _audioMode.value = !_audioMode.value
    }

    fun onSend() {
        messageText.value = ""
    }
}