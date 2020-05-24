@file:Suppress("unused", "KDocUnresolvedReference")
@file:JvmName("PartFactory")

package com.dylanc.retrofit.helper

import okhttp3.MultipartBody
import java.io.File
import java.util.*

/**
 * @author Dylan Cai
 */

@JvmName("create")
fun String.toPart(
  name: String,
  contentType: String = ContentType.MULTIPART
) = File(this).toPart(name, contentType)

@JvmName("create")
fun File.toPart(
  name: String,
  contentType: String = ContentType.MULTIPART
): MultipartBody.Part {
  return MultipartBody.Part.createFormData(name, this.name, asRequestBody(contentType))
}

@JvmName("create")
fun List<String>.toPartList(
  name: String,
  contentType: String = ContentType.MULTIPART
): List<MultipartBody.Part> {
  val list = ArrayList<MultipartBody.Part>()
  for (pathname in this) {
    list.add(pathname.toPart(name, contentType))
  }
  return list
}