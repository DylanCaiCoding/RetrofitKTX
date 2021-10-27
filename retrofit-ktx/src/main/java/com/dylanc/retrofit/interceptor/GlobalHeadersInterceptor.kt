@file:Suppress("unused")

package com.dylanc.retrofit.interceptor

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * @author Dylan Cai
 */

fun OkHttpClient.Builder.addHeaders(vararg pairs: Pair<String, String>): OkHttpClient.Builder =
  addHeaders(mutableMapOf(*pairs))

fun OkHttpClient.Builder.addHeaders(headers: Map<String, String>): OkHttpClient.Builder = apply {
  if (headers.isNotEmpty()) addInterceptor(GlobalHeadersInterceptor(headers))
}

class GlobalHeadersInterceptor(
  private val headers: Map<String, String>
) : HeadersInterceptor() {

  override fun onCreateHeaders(request: Request) = headers
}