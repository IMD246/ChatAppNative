package com.example.chatappnative.presentation.add_contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.api.APIConstants
import com.example.chatappnative.data.api.ResponseState
import com.example.chatappnative.data.local_database.Preferences
import com.example.chatappnative.data.model.ChatDetailParamModel
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.data.model.FriendModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.model.TypeChat
import com.example.chatappnative.data.model.UserPresenceSocketModel
import com.example.chatappnative.domain.repository.ContactRepository
import com.example.chatappnative.helper.DialogAPIHelper
import com.example.chatappnative.service.EventBusService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel
@Inject constructor(
    private val contactRepository: ContactRepository,
    private val preferences: Preferences,
) : ViewModel() {
    val dialogAPIHelper = DialogAPIHelper()

    private val pageSize = APIConstants.PAGE_SIZE

    private val _isLoadingContactList = MutableStateFlow(false)
    val isLoadingContactList = _isLoadingContactList

    private var _pagedContactList = PagedListModel<ContactModel>()

    private val _contactList = MutableStateFlow(arrayOf<ContactModel>().toList())
    val contactList = _contactList

    private var _keyword: String? = null

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _isContactListLoadMore = MutableStateFlow(false)
    var isContactListLoadMore = _isContactListLoadMore

    private val channelEvent = Channel<ChannelEventAddContact>()
    val channelFlow = channelEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            fetchData()
        }
    }

    fun updateItemPresence(value: UserPresenceSocketModel) {
        val contactListUpdated = contactList.value.toMutableList()

        val item = contactListUpdated.find { it.id == value.userId } ?: return

        val index = contactListUpdated.indexOf(item)

        contactListUpdated[index] =
            item.copy(
                presence = value.presence,
                presenceTimestamp = value.presenceTimestamp
            )

        _contactList.value = contactListUpdated
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
            contactRepository.getContactList(
                page = _pagedContactList.currentPage + 1,
                keyword = _keyword,
                pageSize = pageSize,
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
            fetchData(clear = true)
        }
    }

    suspend fun loadMore() {
        if (_isContactListLoadMore.value) return

        if (_pagedContactList.currentPage >= _pagedContactList.totalPages) return

        viewModelScope.launch {
            _isContactListLoadMore.value = true
            fetchData(isLoadMore = true)
            _isContactListLoadMore.value = false
        }
    }

    fun updateItemStatusWithoutAPI(friendId: String, statusUpdate: Int) {
        val data = _contactList.value.toMutableList()
        val item = data.find { it.id == friendId } ?: return

        val index = data.indexOf(item)
        data[index] = data[index].copy(status = statusUpdate)
        _contactList.value = data
    }

    fun updateItemStatus(item: ContactModel, statusUpdate: Int) {
        val data = _contactList.value.toMutableList()

        val index = data.indexOf(item)

        viewModelScope.launch {
            contactRepository.updateFriendStatus(
                friendId = item.id,
                status = statusUpdate,
            ).collect {
                when (it) {
                    is ResponseState.Error -> {
                        channelEvent.send(ChannelEventAddContact.ShowToastMessage("Đã có lỗi xảy ra vui lòng thử lại!"))
                        dialogAPIHelper.hideDialog()
                    }

                    is ResponseState.Loading -> {
                        dialogAPIHelper.showDialog(stateAPI = it)
                    }

                    is ResponseState.Success -> {
                        dialogAPIHelper.hideDialog()
                        channelEvent.send(ChannelEventAddContact.ShowToastMessage("Đã cập nhật thành công!"))

                        data[index] =
                            data[index].copy(status = it.data?.user_status ?: data[index].status)

                        if (data[index].status == 1 || data[index].status == 3) {
                            Log.d("AddContactViewModel", "sendFriendEvent: ${data[index]}")
                            EventBusService.sendFriendEvent(
                                data[index].status,
                                FriendModel(
                                    id = item.id,
                                    name = item.name,
                                    urlImage = item.urlImage,
                                    presence = item.presence,
                                    presenceTimestamp = item.presenceTimestamp,
                                ),
                            )
                        }
                        _contactList.value = data
                    }
                }
            }
        }
    }

    fun getUserInfo() = preferences.getUserInfo()

    fun selectContactItem(it: ContactModel) {
        viewModelScope.launch {
            channelEvent.send(
                ChannelEventAddContact.ClickItem(
                    ChatDetailParamModel(
                        listUserID = listOf(it.id),
                        type = TypeChat.PERSONAL.type,
                    )
                )
            )
        }
    }
}

sealed class ChannelEventAddContact {
    data class ShowToastMessage(val message: String) : ChannelEventAddContact()
    data class ClickItem(val chatDetailParamModel: ChatDetailParamModel) : ChannelEventAddContact()
}