@file:Suppress("unused")

package com.dylanc.retrofit.helper

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * @author Dylan Cai
 * @since 2019/9/28
 */
fun String.toRequestBody(contentType: String = ContentType.JSON) =
  RequestBodyFactory.create(this, contentType)

fun File.asRequestBody(contentType: String = ContentType.JSON) =
  RequestBodyFactory.create(this, contentType)

fun jsonBodyOf(vararg params: Pair<String, Any>) =
  RequestBodyFactory.create(hashMapOf<String, Any>().apply {
    for (pair in params) {
      put(pair.first, pair.second)
    }
  })

object RequestBodyFactory {

  @JvmOverloads
  @JvmStatic
  fun create(content: String, contentType: String = ContentType.JSON) =
    content.toRequestBody(contentType.toMediaTypeOrNull())

  @JvmOverloads
  @JvmStatic
  fun create(file: File, contentType: String = ContentType.MULTIPART) =
    file.asRequestBody(contentType.toMediaTypeOrNull())

  @JvmStatic
  fun create(params: Map<String, Any>) = Gson().toJson(params).toRequestBody()
}