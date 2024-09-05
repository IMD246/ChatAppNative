package com.example.chatappnative.presentation.add_contact.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatappnative.data.model.ContactModel
import com.example.chatappnative.presentation.add_contact.AddContactViewModel
import com.example.chatappnative.presentation.composables.BaseList
import com.example.chatappnative.presentation.composables.NetworkImage
import com.example.chatappnative.ui.theme.Color191919
import com.example.chatappnative.ui.theme.ColorPrimary

@Composable
fun AddContactContent(addContactModel: AddContactViewModel) {
    val items = addContactModel.contactList.collectAsState().value
    val isLoading = addContactModel.isLoadingContactList.collectAsState().value
    val isLoadMore = addContactModel.isContactListLoadMore.collectAsState().value

    BaseList(
        items = items,
        loadingContent = {
            AddContactListShimmer()
        },
        loadMoreContent = {
            AddContactShimmerItem()
        },
        emptyContent = {
            Text(
                text = "There is no any contacts here",
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
            AddContactItem(addContactModel, it)
        },
        keyItem = { it.uuid },
        isLoadMore = isLoadMore,
        onLoadMore = {
            addContactModel.loadMore()
        },
        modifier = Modifier.padding(20.dp, 15.dp),
        horizontalAlignment = Alignment.Start,
    )
}

@Composable
private fun AddContactItem(addContactModel: AddContactViewModel, item: ContactModel) {
    Row(
        modifier = Modifier
            .clickable {
                addContactModel.selectContactItem(item)
            }
            .padding(bottom = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        NetworkImage(
            url = item.urlImage,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = TextStyle(
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                color = Color191919.copy(alpha = 0.95F),
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        ActionAddContact(addContactModel, item)
    }
}

@Composable
private fun ActionAddContact(addContactModel: AddContactViewModel, item: ContactModel) {
    var title = ""
    var titleColor = Color.White
    var bgColor = ColorPrimary
    var statusUpdate = item.status

    when (item.status) {
        1 -> {
            title = "Kết bạn"
            titleColor = Color.White
            bgColor = ColorPrimary
            statusUpdate = 2
        }

        2 -> {
            title = "Hủy yêu cầu"
            titleColor = Color.White.copy(alpha = 0.4F)
            bgColor = ColorPrimary.copy(0.4F)
            statusUpdate = 1
        }

        3 -> {
            title = "Hủy kết bạn"
            titleColor = Color.White
            bgColor = Color.Red
            statusUpdate = 1
        }

        4 -> {
            title = "Đồng ý"
            titleColor = Color.White
            bgColor = Color.Green
            statusUpdate = 3
        }

        else -> {

        }
    }

    OutlinedButton(
        modifier = Modifier.defaultMinSize(
            minWidth = 120.dp,
            minHeight = 48.dp
        ),
        border = ButtonDefaults.outlinedButtonBorder(
            enabled = false,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
        ),
        onClick = {
            addContactModel.updateItemStatus(item, statusUpdate)
        },
    ) {
        Text(
            text = title,
            style = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = titleColor
            )
        )
    }
}