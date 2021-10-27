@file:Suppress("unused")

package com.dylanc.retrofit.body

import android.net.Uri
import com.dylanc.retrofit.app.application
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * @author Dylan Cai
 */

@JvmOverloads
@JvmName("create")
fun File.asRequestBody(contentType: String = "multipart/form-data"): RequestBody =
  asRequestBody(contentType.toMediaTypeOrNull())

@JvmOverloads
@JvmName("create")
fun Uri.asRequestBody(
  contentType: String = "multipart/form-data",
  offset: Int = 0,
  byteCount: Int? = null
): RequestBody {
  val inputStream = checkNotNull(application.contentResolver.openInputStream(this)) { "Unable to create stream" }
  val content = inputStream.readBytes()
  return content.toRequestBody(contentType.toMediaTypeOrNull(), offset, byteCount ?: content.size)
}

typealias RequestMap = @JvmSuppressWildcards Map<String, Any?>

fun requestMapOf(vararg pairs: Pair<String, Any?>) = mutableMapOf(*pairs)