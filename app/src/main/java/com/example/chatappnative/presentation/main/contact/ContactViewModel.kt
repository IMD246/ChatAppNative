package com.example.chatappnative.presentation.main.contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.api.APIConstants
import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.FriendModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.UserInfoAccessModel
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.data.param.ChatDetailParam
import com.example.chatappnative.data.param.TypeChat
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel
@Inject constructor(
    private val socketManager: SocketManager,
    private val contactRepository: ContactRepository,
    private val preferences: Preferences
) : ViewModel() {
    private val pageSize = APIConstants.PAGE_SIZE

    private val _isLoadingContactList = MutableStateFlow(false)
    val isLoadingContactList = _isLoadingContactList

    private var _pagedContactList = PagedListModel<FriendModel>()

    private val _contactList = MutableStateFlow(arrayOf<FriendModel>().toList())
    val contactList = _contactList

    private var _keyword: String? = null

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _isContactListLoadMore = MutableStateFlow(false)
    var isContactListLoadMore = _isContactListLoadMore

    private val _exceptFriendIds = mutableListOf<String>()

    private val channelEvent = Channel<ChannelEventContact>()
    val channelEventFlow = channelEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            fetchData()
        }
    }

    private suspend fun fetchData(
        clear: Boolean = false,
        isLoadMore: Boolean = false,
    ) {
        if (clear) {
            _contactList.value = listOf()
            _pagedContactList = PagedListModel()
        }

        viewModelScope.launch {
            getContactList(isLoadMore)
        }.join()
    }

    private suspend fun getContactList(isLoadMore: Boolean = false) {
        viewModelScope.launch {
            contactRepository.getFriendList(
                page = _pagedContactList.currentPage + 1,
                keyword = _keyword,
                pageSize = pageSize,
                exceptFriendIds = formatExceptFriendIds()
            ).collectLatest {
                when (it) {
                    is ResponseState.Error -> {
                        if (!isLoadMore) {
                            _isLoadingContactList.value = false
                        }
                    }

                    is ResponseState.Loading -> {
                        if (!isLoadMore) {
                            _isLoadingContactList.value = true
                        }
                    }

                    is ResponseState.Success -> {
                        if (!isLoadMore) {
                            _isLoadingContactList.value = false
                        }
                        val data = it.data ?: return@collectLatest

                        _pagedContactList = data
                        _contactList.value = _contactList.value.plus(data.data)
                    }
                }
            }
        }.join()
    }

    fun onRefresh() {
        viewModelScope.launch {
            _exceptFriendIds.clear()
            _isRefreshing.value = true
            fetchData(clear = true)
            _isRefreshing.value = false
        }
    }


    val onSubmitted: (String) -> Unit = {
        _keyword = it.ifBlank {
            null
        }

        viewModelScope.launch {
            _exceptFriendIds.clear()
            fetchData(clear = true)
        }
    }

    suspend fun loadMore() {
        if (_isContactListLoadMore.value) return

        viewModelScope.launch {
            _isContactListLoadMore.value = true
            fetchData(isLoadMore = true)
            _isContactListLoadMore.value = false
        }
    }

    fun updateContactFriend(status: Int, friendModel: FriendModel) {
        Log.d("ContactViewModel", "updateContactFriend: $status, $friendModel")
        // friend status is accepted
        if (status == 3) {
            val data = _contactList.value.toMutableList()

            if (!data.contains(friendModel)) {
                data.add(friendModel)
            }

            if (!_exceptFriendIds.contains(friendModel.id)) {
                _exceptFriendIds.add(friendModel.id)
            }

            _contactList.value = data
        }
        // friend status is unfriend
        if (status == 1) {
            val data = _contactList.value.toMutableList()

            val item = data.find { it.id == friendModel.id } ?: return

            data.remove(item)

            if (_exceptFriendIds.contains(friendModel.id)) {
                _exceptFriendIds.remove(friendModel.id)
            }

            _contactList.value = data
        }
    }

    private fun formatExceptFriendIds(): String? {
        if (_exceptFriendIds.isEmpty()) {
            return null
        }

        return _exceptFriendIds.joinToString(separator = ",")
    }

    fun updateItemPresence(userPresenceSocketModel: UserPresenceSocketModel) {
        val data = _contactList.value.toMutableList()
        val item = data.find { it.id == userPresenceSocketModel.userId } ?: return
        val index = data.indexOf(item)

        data[index] = data[index].copy(
            presence = userPresenceSocketModel.presence,
            presenceTimestamp = userPresenceSocketModel.presenceTimestamp
        )

        _contactList.value = data
    }

    fun getUserInfo(): UserInfoAccessModel? {
        return preferences.getUserInfo()
    }

    fun selectContactItem(friendModel: FriendModel) {
        viewModelScope.launch {
            channelEvent.send(
                ChannelEventContact.ClickItemContact(
                    ChatDetailParam(
                        listUserID = arrayListOf(friendModel.id),
                        type = TypeChat.PERSONAL.type
                    )
                )
            )
        }
    }
}

sealed class ChannelEventContact {
    data class ClickItemContact(val chatDetailParam: ChatDetailParam) :
        ChannelEventContact()
}