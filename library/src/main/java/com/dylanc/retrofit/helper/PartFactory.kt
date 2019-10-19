@file:Suppress("unused")

package com.dylanc.retrofit.helper

import okhttp3.MultipartBody
import java.io.File
import java.util.*

/**
 * @author Dylan Cai
 * @since 2019/7/14
 */
fun String.toPart(name: String, contentType: String = ContentType.MULTIPART) =
  PartFactory.create(name, this, contentType)

fun List<String>.toPartList(name: String, contentType: String = ContentType.MULTIPART) =
  PartFactory.createList(name, this, contentType)

object PartFactory {
  @JvmOverloads
  @JvmStatic
  fun create(
    name: String,
    pathname: String,
    contentType: String = ContentType.MULTIPART
  ): MultipartBody.Part {
    val file = File(pathname)
    val requestBody =  file.asRequestBody(contentType)
    return MultipartBody.Part.createFormData(name, file.name, requestBody)
  }

  @JvmOverloads
  @JvmStatic
  fun createList(
    name: String,
    pathnameList: List<String>,
    contentType: String = ContentType.MULTIPART
  ): List<MultipartBody.Part> {
    val list = ArrayList<MultipartBody.Part>()
    for (pathname in pathnameList) {
      list.add(create(name, pathname, contentType))
    }
    return list
  }

}