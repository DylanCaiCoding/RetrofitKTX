@file:Suppress("unused")

package com.dylanc.retrofit

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Invocation
import retrofit2.Retrofit

inline fun retrofit(crossinline block: Retrofit.Builder.() -> Unit): Retrofit =
  Retrofit.Builder().apply(block).build()

inline fun Retrofit.Builder.okHttpClient(crossinline block: OkHttpClient.Builder.() -> Unit): Retrofit.Builder =
  client(OkHttpClient.Builder().apply(block).build())

inline fun Retrofit.copy(crossinline block: Retrofit.Builder.() -> Unit): Retrofit =
  newBuilder().apply(block).build()

inline fun <reified T : Annotation> Request.getMethodAnnotation(): T? =
  tag(Invocation::class.java)?.method()?.getAnnotation(T::class.java)
