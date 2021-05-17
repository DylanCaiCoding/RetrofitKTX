@file:Suppress("unused")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * @author Dylan Cai
 */
inline fun OkHttpClient.Builder.addHeaders(crossinline onCreateHeaders: (Request) -> Map<String, String>) =
  addInterceptor(object : BaseHeadersInterceptor() {
    override fun onCreateHeaders(request: Request) = onCreateHeaders(request)
  })

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