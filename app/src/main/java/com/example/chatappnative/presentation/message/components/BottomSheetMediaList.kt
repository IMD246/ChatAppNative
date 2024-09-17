package com.example.chatappnative.presentation.message.components

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.chatappnative.R
import com.example.chatappnative.presentation.composables.BaseGrid
import com.example.chatappnative.presentation.message.MessageViewModel
import com.example.chatappnative.ui.theme.ColorPrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@Composable
fun MediaGrid(
    messageViewModel: MessageViewModel
) {
    val mediaList = messageViewModel.mediaListFlow.collectAsState().value

    BaseGrid(
        items = mediaList,
        columns = 4,
        emptyContent = {},
        loadingContent = {},
        loadMoreContent = {},
        contentItem = { item, itemSize ->
            DisplayMedia(item, itemSize, messageViewModel)
        },
    )
}

@Composable
fun DisplayMedia(
    uri: Uri,
    itemSize: Dp,
    messageViewModel: MessageViewModel,
) {
    val context = LocalContext.current

    val mimeType = context.contentResolver.getType(uri)

    val selectedMediaList = messageViewModel.selectedMediaListFlow.collectAsState().value

    Box(
        modifier = Modifier
            .size(itemSize)
            .clickable {
                messageViewModel.onSelectedMediaItem(uri)
            }
            .background(Color.Black.copy(alpha = 0.5F))
            .padding(horizontal = 1.dp),
        contentAlignment = Alignment.Center
    ) {
        if (mimeType?.startsWith("image") == true) {
            DisplayImage(uri)
        } else if (mimeType?.startsWith("video") == true) {
            DisplayVideo(uri = uri, itemSize = itemSize)
        }

        if (selectedMediaList.contains(uri)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5F)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = (selectedMediaList.indexOf(uri) + 1).toString(),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun DisplayImage(
    uri: Uri
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = uri,
            placeholder = painterResource(id = R.drawable.ic_photo),
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun DisplayVideo(
    uri: Uri,
    itemSize: Dp
) {
    VideoThumbnailWithDuration(uri = uri, size = itemSize)
}

@SuppressLint("DefaultLocale")
@Composable
fun VideoThumbnailWithDuration(uri: Uri, size: Dp) {
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<ImageBitmap?>(null) }
    val videoDuration = remember { mutableStateOf("") }

    LaunchedEffect(uri) {
        val duration = withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, uri)
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLongOrNull()
            } finally {
                retriever.release()
            }
        }

        val getBitMap = withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, uri)
                retriever.frameAtTime?.asImageBitmap()
            } finally {
                retriever.release()
            }
        }

        getBitMap?.let {
            bitmap.value = it
        }

        duration?.let {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(it)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(it) % 60
            videoDuration.value = String.format("%02d:%02d", minutes, seconds)
        }
    }

    // Box layout to overlay duration text on the video thumbnail
    Box(
        modifier = Modifier
            .size(size)
    ) {
        // Display the video thumbnail
        bitmap.value?.let { thumb ->
            Image(
                bitmap = thumb,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } ?: Image(
            painter = painterResource(id = R.drawable.ic_photo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Display the video duration at the bottom-right corner
        if (videoDuration.value.isNotEmpty()) {
            Text(
                text = videoDuration.value,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)
                    .background(Color.Black.copy(alpha = 0.3f), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetMediaGrid(
    messageViewModel: MessageViewModel
) {
    val sheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()
    val selectedMedia = messageViewModel.selectedMediaListFlow.collectAsState().value

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                messageViewModel.onClosePhotoSheet()
            }
        },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .weight(1F)
                        .fillMaxHeight(0.07F),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Gray,
                        contentColor = Color.White,
                    ),
                    onClick = {
                        scope.launch {
                            messageViewModel.cancelSelectedMedia()
                        }
                    },
                ) {
                    Text(
                        text = "CLEAR",
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight.Bold, fontSize = 18.sp,
                        ),
                    )
                }
                if (selectedMedia.isNotEmpty())
                    OutlinedButton(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .weight(1F)
                            .fillMaxHeight(0.07F),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = ColorPrimary,
                            contentColor = Color.White,
                        ),
                        onClick = {
                            scope.launch {
                                sheetState.hide()
                                messageViewModel.onClosePhotoSheet()
                                messageViewModel.onSendMedia()
                            }
                        },
                    ) {
                        Text(
                            text = "SEND ${selectedMedia.size}",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold, fontSize = 18.sp,
                            ),
                        )
                    }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 20.dp)
                    .fillMaxHeight(0.9F),
                contentAlignment = Alignment.Center
            ) {
                MediaGrid(messageViewModel)
            }
        }
    )
}