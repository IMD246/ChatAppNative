package com.example.chatappnative.presentation.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _tabbarIndex = MutableStateFlow(0)
    val tabbarIndex = _tabbarIndex

    fun setTabbarIndex(index: Int) {
        _tabbarIndex.value = index
    }
}