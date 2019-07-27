package com.dylanc.retrofit.helper

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

/**
 * @author Dylan Cai
 * @since 2019/7/14
 */
class UploadUtils {
  companion object {
    @JvmStatic
    fun createTextRequestBody(content: String) = content.toRequestBody("text/plain".toMediaTypeOrNull())

    @JvmStatic
    fun createPart(contentType: String, name: String, pathname: String): MultipartBody.Part {
      val file = File(pathname)
      val requestBody = file.asRequestBody(contentType.toMediaTypeOrNull())
      return MultipartBody.Part.createFormData(name, file.name, requestBody)
    }

    @JvmStatic
    fun createPartList(contentType: String, name: String, pathnameList: List<String>): List<MultipartBody.Part> {
      val partList = ArrayList<MultipartBody.Part>()
      for (pathname in pathnameList) {
        partList.add(createPart(contentType, name, pathname))
      }
      return partList
    }
  }
}