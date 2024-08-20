package com.example.chatappnative.presentation.main.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatappnative.data.ResponseState
import com.example.chatappnative.data.api.APIConstants
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.data.model.PagedListModel
import com.example.chatappnative.data.socket.SocketManager
import com.example.chatappnative.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel
@Inject constructor(
    private val socketManager: SocketManager,
    private val contactRepository: ContactRepository,
) : ViewModel() {
    private val pageSize = APIConstants.PAGE_SIZE

    private val _isLoadingContactList = MutableStateFlow(false)
    val isLoadingContactList = _isLoadingContactList

    private var _pagedContactList = PagedListModel<ContactModel>()

    private val _contactList = MutableStateFlow(arrayOf<ContactModel>().toList())
    val chatList = _contactList

    private var _keyword: String? = null

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing

    private val _isContactListLoadMore = MutableStateFlow(false)
    var isContactListLoadMore = _isContactListLoadMore

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

        viewModelScope.launch {
            _isContactListLoadMore.value = true
            fetchData(isLoadMore = true)
            _isContactListLoadMore.value = false
        }
    }
}