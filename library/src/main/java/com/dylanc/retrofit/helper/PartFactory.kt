package com.dylanc.retrofit.helper

import okhttp3.MultipartBody
import java.io.File
import java.util.ArrayList

/**
 * @author Dylan Cai
 * @since 2019/7/14
 */
object PartFactory {

  private const val CONTENT_TYPE_DEFAULT = "multipart/form-data"

  @JvmStatic
  fun create(name: String, pathname: String): MultipartBody.Part {
    return create(name, pathname, CONTENT_TYPE_DEFAULT)
  }

  @JvmStatic
  fun create(name: String, pathname: String, contentType: String): MultipartBody.Part {
    val file = File(pathname)
    val requestBody = RequestBodyFactory.create(file, contentType)
    return MultipartBody.Part.createFormData(name, file.name, requestBody)
  }

  @JvmStatic
  fun createList(
    name: String,
    pathnameList: List<String>
  ) = createList(name, pathnameList, CONTENT_TYPE_DEFAULT)

  @JvmStatic
  fun createList(
    name: String,
    pathnameList: List<String>,
    contentType: String
  ): List<MultipartBody.Part> {
    val list = ArrayList<MultipartBody.Part>()
    for (pathname in pathnameList) {
      list.add(create(name, pathname, contentType))
    }
    return list
  }

}