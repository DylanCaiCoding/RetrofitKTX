@file:Suppress("unused")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author Dylan Cai
 */
abstract class BaseHeadersInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val newRequest = request.newBuilder()
      .method(request.method, request.body)
      .apply {
        val headers = onCreateHeaders(request)
        for ((key, value) in headers) {
          header(key, value)
        }
      }
      .build()
    return chain.proceed(newRequest)
  }

  abstract fun onCreateHeaders(request: Request): Map<String, String>
}