@file:JvmName("ResponseBodyUtils")

package com.dylanc.retrofit.helper.body

import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 */

fun ResponseBody.toFile(pathname: String): File =
  File(pathname).apply {
    use {
      outputStream().use { fileOut ->
        byteStream().copyTo(fileOut)
      }
    }
  }