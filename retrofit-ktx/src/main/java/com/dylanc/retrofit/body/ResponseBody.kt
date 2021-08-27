@file:JvmName("ResponseBodyUtils")
@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.body

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.dylanc.retrofit.app.application
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 */

inline fun ResponseBody.toFile(pathname: String): File =
  use {
    File(pathname).apply {
      outputStream().use { byteStream().copyTo(it) }
    }
  }

inline fun ResponseBody.toMediaImageUri(noinline block: ContentValues.() -> Unit = {}): Uri =
  toMediaUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, block)

inline fun ResponseBody.toMediaVideoUri(noinline block: ContentValues.() -> Unit = {}): Uri =
  toMediaUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, block)

inline fun ResponseBody.toMediaAudioUri(noinline block: ContentValues.() -> Unit = {}): Uri =
  toMediaUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, block)

fun ResponseBody.toMediaUri(uri: Uri, block: ContentValues.() -> Unit = {}): Uri =
  use {
    ContentValues().apply(block)
      .let { application.contentResolver.insert(uri, it)!! }
      .apply {
        use { byteStream().copyTo(application.contentResolver.openOutputStream(this)!!) }
      }
  }

inline var ContentValues.displayName: String?
  get() = get(MediaStore.MediaColumns.DISPLAY_NAME) as String?
  set(value) {
    put(MediaStore.MediaColumns.DISPLAY_NAME, value)
  }

inline var ContentValues.relativePath: String?
  get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    get(MediaStore.MediaColumns.RELATIVE_PATH) as String?
  } else {
    null
  }
  @SuppressLint("InlinedApi")
  set(value) {
    put(MediaStore.MediaColumns.RELATIVE_PATH, value)
  }