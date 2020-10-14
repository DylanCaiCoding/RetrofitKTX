@file:Suppress("unused", "NOTHING_TO_INLINE")
@file:JvmName("PartFactory")

package com.dylanc.retrofit.helper.body

import okhttp3.MultipartBody
import java.io.File
import java.util.*

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
inline fun List<String>.toPartList(name: String, contentType: String = "multipart/form-data"): List<MultipartBody.Part> {
  val list = ArrayList<MultipartBody.Part>()
  for (pathname in this) {
    list.add(pathname.toPart(name, contentType))
  }
  return list
}