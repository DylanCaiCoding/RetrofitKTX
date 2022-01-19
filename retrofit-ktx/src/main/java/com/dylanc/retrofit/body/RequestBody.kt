@file:JvmName("RequestBodyUtils")

package com.dylanc.retrofit.body

import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@JvmOverloads
@JvmName("create")
fun Uri.asRequestBody(
  context: Context,
  contentType: String = "multipart/form-data",
  offset: Int = 0,
  byteCount: Int? = null
): RequestBody =
  asRequestBody(context, contentType.toMediaTypeOrNull(), offset, byteCount)

@JvmOverloads
@JvmName("create")
fun Uri.asRequestBody(
  context: Context,
  mediaType: MediaType?,
  offset: Int = 0,
  byteCount: Int? = null
): RequestBody {
  val inputStream = context.contentResolver.openInputStream(this)
  checkNotNull(inputStream) { "The uri cannot create input stream." }
  val content = inputStream.readBytes()
  return content.toRequestBody(mediaType, offset, byteCount ?: content.size)
}
