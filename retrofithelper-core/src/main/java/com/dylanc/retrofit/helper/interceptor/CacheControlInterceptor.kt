@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.io.File

inline fun cacheControl(noinline block: CacheControl.Builder.() -> Unit): CacheControl =
  CacheControl.Builder().apply(block).build()

inline fun OkHttpClient.Builder.cache(
  directory: File,
  maxSize: Long,
  noinline onCreateCacheControl: () -> CacheControl?
) =
  apply {
    cache(Cache(directory, maxSize))
    addNetworkInterceptor(CacheControlInterceptor(onCreateCacheControl))
  }

class CacheControlInterceptor(
  private val onCreateCacheControl: () -> CacheControl?
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain) =
    chain.proceed(chain.request()).newBuilder()
      .removeHeader("Pragma")
      .apply {
        val cacheControl = onCreateCacheControl()
        if (cacheControl != null) {
          removeHeader("Cache-Control")
          header("Cache-Control", cacheControl.toString())
        }
      }
      .build()
}