@file:JvmName("ResponseBodyUtils")
@file:Suppress("unused")

package com.dylanc.retrofit.body

import android.content.Context
import android.net.Uri
import okhttp3.ResponseBody
import java.io.File

fun ResponseBody.toFile(block: () -> File): File =
  use {
    block().apply {
      outputStream().use { byteStream().copyTo(it) }
    }
  }

fun ResponseBody.toUri(context: Context, block: () -> Uri): Uri =
  use {
    block().apply {
      val outputStream = context.contentResolver.openOutputStream(this)
      checkNotNull(outputStream) { "The uri cannot create output stream." }
      use { byteStream().copyTo(outputStream) }
    }
  }
