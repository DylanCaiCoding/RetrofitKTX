@file:JvmName("PartFactory")
@file:Suppress("unused")

package com.dylanc.retrofit.body

import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @author Dylan Cai
 */

@JvmOverloads
@JvmName("create")
fun File.toPart(name: String, contentType: String = "multipart/form-data"): MultipartBody.Part =
  asRequestBody(contentType).toPart(name, this.name)

@JvmOverloads
@JvmName("create")
fun Uri.toPart(
  name: String,
  filename: String? = null,
  contentType: String = "multipart/form-data",
  offset: Int = 0,
  byteCount: Int? = null
): MultipartBody.Part =
  asRequestBody(contentType, offset, byteCount).toPart(name, filename)

@JvmOverloads
@JvmName("create")
fun RequestBody.toPart(name: String, filename: String? = null): MultipartBody.Part =
  MultipartBody.Part.createFormData(name, filename, this)

@JvmOverloads
@JvmName("create")
fun List<Uri>.toPartList(name: String, contentType: String = "multipart/form-data") = map {
  it.toPart(name, contentType)
}