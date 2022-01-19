@file:JvmName("PartUtils")
@file:Suppress("unused")

package com.dylanc.retrofit.body

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@JvmOverloads
@JvmName("create")
fun File.asPart(name: String, contentType: String = "multipart/form-data"): MultipartBody.Part {
  val requestBody = asRequestBody(contentType.toMediaTypeOrNull())
  return MultipartBody.Part.createFormData(name, this.name, requestBody)
}

@JvmOverloads
@JvmName("create")
fun Uri.asPart(
  context: Context, name: String, filename: String? = null,
  contentType: String = "multipart/form-data",
  offset: Int = 0, byteCount: Int? = null
): MultipartBody.Part {
  val requestBody = asRequestBody(context, contentType, offset, byteCount)
  return MultipartBody.Part.createFormData(name, filename, requestBody)
}
