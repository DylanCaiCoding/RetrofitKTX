@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * @author Dylan Cai
 */

inline fun OkHttpClient.Builder.addHeaders(vararg pairs: Pair<String, String>): OkHttpClient.Builder =
  addHeaders(mutableMapOf(*pairs))

inline fun OkHttpClient.Builder.addHeaders(headers: Map<String, String>): OkHttpClient.Builder = apply {
  if (headers.isNotEmpty()) addInterceptor(GlobalHeadersInterceptor(headers))
}

class GlobalHeadersInterceptor(
  private val headers: Map<String, String>
) : BaseHeadersInterceptor() {

  override fun onCreateHeaders(request: Request) = headers
}