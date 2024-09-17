package com.example.chatappnative.service

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

class MediaService(
    private val context: Context,
) {
    fun getAllMedia(): List<Uri> {
        return getImageList() +
                getVideoList()
    }

    fun getImageList(): List<Uri> {
        val mediaList = mutableListOf<Uri>()

        // Query media files using ContentResolver with limit and offset
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = MediaStore.Images.Media.DATE_ADDED

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, // No selection clause
            null, // No selection arguments
            sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                mediaList.add(contentUri)
            }
        }

        return mediaList
    }

    fun getVideoList(): List<Uri> {
        val mediaList = mutableListOf<Uri>()

        // Query media files using ContentResolver with limit and offset
        val videoProjection = arrayOf(MediaStore.Video.Media._ID)
        val videoSortOrder = MediaStore.Video.Media.DATE_ADDED

        val videoCursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            null, // No selection clause
            null, // No selection arguments
            videoSortOrder
        )

        videoCursor?.use {
            val idColumn = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            while (videoCursor.moveToNext()) {
                val id = videoCursor.getLong(idColumn)
                val videoUri =
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                mediaList.add(videoUri)
            }
        }

        return mediaList
    }
}