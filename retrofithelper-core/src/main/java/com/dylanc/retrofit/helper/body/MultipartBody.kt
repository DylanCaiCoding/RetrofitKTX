@file:Suppress("unused", "NOTHING_TO_INLINE")
@file:JvmName("PartFactory")

package com.dylanc.retrofit.helper.body

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * @author Dylan Cai
 */

@JvmOverloads
@JvmName("create")
inline fun String.toPart(name: String, contentType: String = "multipart/form-data"): MultipartBody.Part =
  File(this).toPart(name, contentType)

@JvmOverloads
@JvmName("create")
inline fun File.toPart(name: String, contentType: String = "multipart/form-data"): MultipartBody.Part =
  MultipartBody.Part.createFormData(name, this.name, asRequestBody(contentType))

@JvmOverloads
@JvmName("create")
inline fun List<String>.toPartList(name: String, contentType: String = "multipart/form-data") = map {
  it.toPart(name, contentType)
}

@JvmOverloads
@JvmName("create")
inline fun File.asRequestBody(contentType: String = "multipart/form-data"): RequestBody =
  asRequestBody(contentType.toMediaTypeOrNull())