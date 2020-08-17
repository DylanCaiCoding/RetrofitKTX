@file:Suppress("unused")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import java.io.IOException
import java.util.HashMap

/**
 * @author Dylan Cai
 */

fun OkHttpClient.Builder.addHeaders(vararg pairs: Pair<String,String>): OkHttpClient.Builder =
  addHeaders(hashMapOf(*pairs))

fun OkHttpClient.Builder.addHeaders(headers: HashMap<String, String>): OkHttpClient.Builder = apply {
  if (headers.isNotEmpty()) addInterceptor(HeaderInterceptor(headers))
}

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
