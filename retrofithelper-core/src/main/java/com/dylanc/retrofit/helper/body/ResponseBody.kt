@file:JvmName("ResponseBodyUtils")
@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.body

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 */

inline fun ResponseBody.toFile(pathname: String): File =
  File(pathname).apply {
    use {
      outputStream().use { fileOut ->
        byteStream().copyTo(fileOut)
      }
    }
  }

inline fun ResponseBody.toImageUri(
  context: Context,
  displayName: String? = null,
  relativePath: String? = null,
  block: ContentValues.() -> Unit = {}
): Uri =
  toMediaUri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
    put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
    put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
    apply(block)
  }

inline fun ResponseBody.toVideoUri(
  context: Context,
  displayName: String? = null,
  relativePath: String? = null,
  block: ContentValues.() -> Unit = {}
): Uri =
  toMediaUri(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
    put(MediaStore.Video.Media.DISPLAY_NAME, displayName)
    put(MediaStore.Video.Media.RELATIVE_PATH, relativePath)
    apply(block)
  }

inline fun ResponseBody.toAudioUri(
  context: Context,
  displayName: String? = null,
  relativePath: String? = null,
  block: ContentValues.() -> Unit = {}
): Uri =
  toMediaUri(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
    put(MediaStore.Audio.Media.DISPLAY_NAME, displayName)
    put(MediaStore.Audio.Media.RELATIVE_PATH, relativePath)
    apply(block)
  }

inline fun ResponseBody.toMediaUri(
  context: Context,
  uri: Uri,
  block: ContentValues.() -> Unit = {}
): Uri =
  ContentValues().apply(block).let {
    context.contentResolver.insert(uri, it)!!
  }.apply {
    use {
      byteStream().copyTo(context.contentResolver.openOutputStream(this)!!)
    }
  }