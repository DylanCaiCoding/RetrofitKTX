package com.dylanc.retrofit.interceptor

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun OkHttpClient.Builder.printHttpLog(
  debug: Boolean = true,
  level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
  logger: HttpLoggingInterceptor.Logger = HttpLoggingInterceptor.Logger { Log.d("Http", it) }
) = apply {
  if (debug) addInterceptor(HttpLoggingInterceptor(logger).setLevel(level))
}
