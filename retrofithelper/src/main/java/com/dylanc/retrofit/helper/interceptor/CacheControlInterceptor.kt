@file:Suppress("unused")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.CacheControl
import okhttp3.Interceptor

fun cacheControlOf(block: CacheControl.Builder.() -> Unit): CacheControl =
  CacheControl.Builder().apply(block).build()

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