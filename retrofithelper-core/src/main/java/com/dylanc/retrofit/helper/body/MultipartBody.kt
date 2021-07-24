@file:Suppress("unused", "NOTHING_TO_INLINE")
@file:JvmName("PartFactory")

package com.dylanc.retrofit.helper.body

import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author Dylan Cai
 */

@JvmOverloads
@JvmName("create")
inline fun File.toPart(name: String, contentType: String = "multipart/form-data"): MultipartBody.Part =
  asRequestBody(contentType).toPart(name, this.name)

@JvmOverloads
@JvmName("create")
inline fun Uri.toPart(
  name: String,
  filename: String? = null,
  contentType: String = "multipart/form-data",
  offset: Int = 0,
  byteCount: Int? = null
): MultipartBody.Part =
  toRequestBody(contentType, offset, byteCount).toPart(name, filename)

@JvmOverloads
@JvmName("create")
inline fun RequestBody.toPart(name: String, filename: String? = null): MultipartBody.Part =
  MultipartBody.Part.createFormData(name, filename, this)

@JvmOverloads
@JvmName("create")
inline fun List<Uri>.toPartList(name: String, contentType: String = "multipart/form-data") = map {
  it.toPart(name, contentType)
}