package com.dylanc.retrofit.helper.interceptor

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author Dylan Cai
 */

fun OkHttpClient.Builder.addHttpLog(
  level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
  log: (message: String) -> Unit
) = addHttpLog(level, object : HttpLoggingInterceptor.Logger {
  override fun log(message: String) = log(message)
})

fun OkHttpClient.Builder.addHttpLog(
  level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
  logger: HttpLoggingInterceptor.Logger
) = apply {
  addInterceptor(HttpLoggingInterceptor(logger).apply { this.level = level })
}


