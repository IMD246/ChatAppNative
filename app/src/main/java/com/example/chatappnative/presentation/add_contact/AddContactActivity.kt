package com.example.chatappnative.presentation.add_contact

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatappnative.event.AddContactEvent
import com.example.chatappnative.presentation.add_contact.components.AddContactContent
import com.example.chatappnative.presentation.composables.BackButton
import com.example.chatappnative.presentation.composables.BaseSearchBar
import com.example.chatappnative.service.EventBusService
import com.example.chatappnative.ui.theme.ChatAppNativeTheme
import com.example.chatappnative.ui.theme.ColorPrimary
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class AddContactActivity : ComponentActivity() {
    private val addContactModel: AddContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChatAppNativeTheme {
                AddContactScreen(addContactViewModel = addContactModel, context = this)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AddContactEvent) {
        addContactModel.updateItemStatusWithoutAPI(
            event.friendStatusModel.friendId,
            event.friendStatusModel.friendStatus
        )
    }

    override fun onStart() {
        super.onStart()
        EventBusService.register(this)
    }

    override fun onStop() {
        EventBusService.unregister(this)
        super.onStop()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(addContactViewModel: AddContactViewModel, context: Context) {
    val message = addContactViewModel.message.collectAsState().value
    val dialogAPIHelper = addContactViewModel.dialogAPIHelper

    dialogAPIHelper.DisplayDialog()

    if (message.isNotEmpty()) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        addContactViewModel.updateShowError()
    }

    val isRefreshing = addContactViewModel.isRefreshing.collectAsState().value

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            addContactViewModel.onRefresh()
        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AppBar(addContactViewModel)
                AddContactContent(addContactViewModel)
            }
        }
    }
}

@Composable
fun AppBar(addContactViewModel: AddContactViewModel) {
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    bottomEnd = 25.dp,
                    bottomStart = 25.dp
                )
            )
            .background(ColorPrimary)
    ) {
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            BackButton()
            Spacer(modifier = Modifier.height(10.dp))
            BaseSearchBar(
                hint = "Search by name, number...",
                onSubmitted = addContactViewModel.onSubmitted,
                onClear = addContactViewModel.onSubmitted
            )
            Spacer(modifier = Modifier.height(35.dp))
        }
    }
}