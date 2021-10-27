package com.dylanc.retrofit.interceptor

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author Dylan Cai
 */

fun OkHttpClient.Builder.addHttpLog(
  level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
  logger: HttpLoggingInterceptor.Logger
) =
  addInterceptor(HttpLoggingInterceptor(logger).apply { this.level = level })