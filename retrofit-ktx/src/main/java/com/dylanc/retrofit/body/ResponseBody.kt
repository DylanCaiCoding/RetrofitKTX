@file:JvmName("ResponseBodyUtils")
@file:Suppress("unused")

package com.dylanc.retrofit.body

import android.net.Uri
import com.dylanc.retrofit.app.application
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 */

fun ResponseBody.toFile(block: () -> File): File =
  use {
    block().apply {
      outputStream().use { byteStream().copyTo(it) }
    }
  }

fun ResponseBody.toUri(block: () -> Uri): Uri =
  use {
    block().apply {
      use { byteStream().copyTo(application.contentResolver.openOutputStream(this)!!) }
    }
  }
