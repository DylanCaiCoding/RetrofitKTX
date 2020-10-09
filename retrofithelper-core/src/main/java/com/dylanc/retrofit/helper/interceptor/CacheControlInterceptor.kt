@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.*
import java.io.File

inline fun cacheControl(noinline block: CacheControl.Builder.() -> Unit): CacheControl =
  CacheControl.Builder().apply(block).build()

inline fun OkHttpClient.Builder.cache(directory: File, maxSize: Long, noinline onCreateCacheControl: (String) -> CacheControl?) =
  apply {
    cache(Cache(directory, maxSize))
    addNetworkInterceptor(CacheControlInterceptor(onCreateCacheControl))
  }

class CacheControlInterceptor(
  private val onCreateCacheControl: (String) -> CacheControl?
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val url = request.url.toString()
    return chain.proceed(request).newBuilder()
      .removeHeader("Pragma")
      .apply {
        val cacheControl = onCreateCacheControl(url)
        if (cacheControl != null) {
          removeHeader("Cache-Control")
          header("Cache-Control", cacheControl.toString())
        }
      }
      .build()
  }
}