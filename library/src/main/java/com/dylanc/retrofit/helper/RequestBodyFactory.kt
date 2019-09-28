package com.dylanc.retrofit.helper

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * @author Dylan Cai
 * @since 2019/9/28
 */
object RequestBodyFactory {

  @JvmStatic
  fun create(content: String) = create(content, "text/plain")

  @JvmStatic
  fun create(content: String, contentType: String) =
    content.toRequestBody(contentType.toMediaTypeOrNull())

  @JvmStatic
  fun create(file: File) = create(file, "multipart/form-data")

  @JvmStatic
  fun create(file: File, contentType: String) =
    file.asRequestBody(contentType.toMediaTypeOrNull())
}