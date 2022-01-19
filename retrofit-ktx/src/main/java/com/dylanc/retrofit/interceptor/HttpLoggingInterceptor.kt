package com.dylanc.retrofit.interceptor

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun OkHttpClient.Builder.printHttpLog(
  level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
  logger: HttpLoggingInterceptor.Logger
) =
  addInterceptor(HttpLoggingInterceptor(logger).setLevel(level))