@file:JvmName("ResponseBodyUtils")
@file:Suppress("unused")

package com.dylanc.retrofit.body

import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.dylanc.retrofit.app.application
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 */

fun ResponseBody.toFile(pathname: String): File =
  use {
    File(pathname).apply {
      outputStream().use { byteStream().copyTo(it) }
    }
  }

fun ResponseBody.toMediaImageUri(block: (ContentValues.() -> Unit)? = null): Uri =
  toMediaUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, block)

fun ResponseBody.toMediaVideoUri(block: (ContentValues.() -> Unit)? = null): Uri =
  toMediaUri(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, block)

fun ResponseBody.toMediaAudioUri(block: (ContentValues.() -> Unit)? = null): Uri =
  toMediaUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, block)

fun ResponseBody.toMediaUri(uri: Uri, block: (ContentValues.() -> Unit)? = null): Uri =
  use {
    ContentValues().run {
        block?.invoke(this)
        application.contentResolver.insert(uri, this)!!
      }
      .apply {
        Environment.DIRECTORY_DCIM
        use { byteStream().copyTo(application.contentResolver.openOutputStream(this)!!) }
      }
  }
