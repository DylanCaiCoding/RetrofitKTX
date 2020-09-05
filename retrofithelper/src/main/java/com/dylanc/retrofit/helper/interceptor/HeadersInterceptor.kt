@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

/**
 * @author Dylan Cai
 */

inline fun OkHttpClient.Builder.addHeaders(vararg pairs: Pair<String, String>): OkHttpClient.Builder =
  addHeaders(mutableMapOf(*pairs))

inline fun OkHttpClient.Builder.addHeaders(headers: MutableMap<String, String>): OkHttpClient.Builder = apply {
  if (headers.isNotEmpty()) addInterceptor(HeadersInterceptor(headers))
}

class HeadersInterceptor(
  val headers: MutableMap<String, String>
) : Interceptor {

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val builder = request.newBuilder()
      .method(request.method, request.body)
      .apply {
        for ((key, value) in headers) {
          header(key, value)
        }
      }
    return chain.proceed(builder.build())
  }
}
