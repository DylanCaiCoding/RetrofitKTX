@file:Suppress("unused")
@file:JvmName("RequestBodyFactory")

package com.dylanc.retrofit.helper

import com.google.gson.Gson
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
fun String.toRequestBody(contentType: String = ContentType.JSON): RequestBody =
  toRequestBody(contentType.toMediaTypeOrNull())

@JvmName("create")
fun Any.toJsonBody() =
  Gson().toJson(this).toRequestBody()

@JvmOverloads
@JvmName("create")
fun File.asRequestBody(contentType: String = ContentType.JSON): RequestBody =
  asRequestBody(contentType.toMediaTypeOrNull())

@JvmOverloads
@JvmName("create")
fun jsonBodyOf(vararg params: Pair<String, Any>, block: HashMap<String, Any>.() -> Unit = {}): RequestBody =
  hashMapOf(*params).apply(block).toJsonBody()
