@file:Suppress("unused")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.HashMap

/**
 * @author Dylan Cai
 */
class HeaderInterceptor @JvmOverloads constructor(
  private val headers: HashMap<String, String> = HashMap()
) : Interceptor {

  fun addHeader(name: String, value: String) {
    headers[name] = value
  }

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
