@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.body

import android.net.Uri
import com.dylanc.retrofit.helper.application
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
inline fun File.asRequestBody(contentType: String = "multipart/form-data"): RequestBody =
  asRequestBody(contentType.toMediaTypeOrNull())

@JvmOverloads
@JvmName("create")
fun Uri.toRequestBody(
  contentType: String = "multipart/form-data",
  offset: Int = 0,
  byteCount: Int? = null
): RequestBody {
  val inputStream = checkNotNull(application.contentResolver.openInputStream(this)) { "Unable to create stream" }
  val content = inputStream.readBytes()
  return content.toRequestBody(contentType.toMediaTypeOrNull(), offset, byteCount ?: content.size)
}

typealias RequestMap = @JvmSuppressWildcards Map<String, Any?>

inline fun requestMapOf(vararg pairs: Pair<String, Any?>) = mutableMapOf(*pairs)