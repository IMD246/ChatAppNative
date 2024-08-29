package com.example.chatappnative.presentation.main.contact.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.data.model.FriendModel
import com.example.chatappnative.presentation.composables.BaseList
import com.example.chatappnative.presentation.composables.NetworkImage
import com.example.chatappnative.presentation.composables.Presence
import com.example.chatappnative.presentation.main.contact.ContactViewModel
import com.example.chatappnative.ui.theme.Color191919

@Composable
fun ContactContent(contactModel: ContactViewModel) {
    val items = contactModel.contactList.collectAsState().value
    val isLoading = contactModel.isLoadingContactList.collectAsState().value
    val isLoadMore = contactModel.isContactListLoadMore.collectAsState().value

    BaseList(
        items = items,
        loadingContent = {
            ContactListShimmer()
        },
        loadMoreContent = {
            ContactShimmerItem()
        },
        emptyContent = {
            Text(
                text = "There is no any friends here \n" +
                        "lets add some",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color191919.copy(alpha = 0.95F),
                    textAlign = TextAlign.Center,
                )
            )
        },
        isLoading = isLoading,
        contentItem = {
            ContactItem(it)
        },
        isLoadMore = isLoadMore,
        onLoadMore = {
            contactModel.loadMore()
        },
        modifier = Modifier.padding(20.dp, 15.dp),
        horizontalAlignment = Alignment.Start,
    )
}

@Composable
private fun ContactItem(item: FriendModel) {
    Row(
        modifier = Modifier.padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Column {
            NetworkImage(
                url = item.urlImage ?: "",
            )
            Presence(isPresence = item.presence, date = item.getDateTimePresence())
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.name,
            style = TextStyle(
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                color = Color191919.copy(alpha = 0.95F),
            )
        )
    }
}