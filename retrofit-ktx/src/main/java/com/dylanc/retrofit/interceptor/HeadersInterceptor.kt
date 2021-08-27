@file:Suppress("unused")

package com.dylanc.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * @author Dylan Cai
 */
inline fun OkHttpClient.Builder.addHeaders(
  crossinline block: MutableMap<String, String>.(Request) -> Unit
) =
  addInterceptor(object : HeadersInterceptor() {
    override fun onCreateHeaders(request: Request) =
      mutableMapOf<String, String>().apply { block(request) }
  })

abstract class HeadersInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val newRequest = request.newBuilder()
      .method(request.method, request.body)
      .apply {
        val headers = onCreateHeaders(request)
        for ((name, value) in headers) {
          header(name, value)
        }
      }
      .build()
    return chain.proceed(newRequest)
  }

  abstract fun onCreateHeaders(request: Request): Map<String, String>
}