@file:Suppress("NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author Dylan Cai
 */

inline fun OkHttpClient.Builder.addHttpLog(
  level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
  noinline log: (message: String) -> Unit
) = addHttpLog(level, object : HttpLoggingInterceptor.Logger {
  override fun log(message: String) = log(message)
})

inline fun OkHttpClient.Builder.addHttpLog(
  level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
  logger: HttpLoggingInterceptor.Logger
) =
  addInterceptor(HttpLoggingInterceptor(logger).apply { this.level = level })
