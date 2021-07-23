@file:Suppress("unused")

package com.dylanc.retrofit.helper

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Invocation
import retrofit2.Retrofit

/**
 * @author Dylan Cai
 */

inline fun retrofit(block: Retrofit.Builder.() -> Unit): Retrofit =
  Retrofit.Builder().apply(block).build()

inline fun Retrofit.Builder.okHttpClient(block: OkHttpClient.Builder.() -> Unit): Retrofit.Builder =
  client(OkHttpClient.Builder().apply(block).build())

inline fun Retrofit.createRetrofit(url: String, block: Retrofit.Builder.() -> Unit = {}): Retrofit =
  newBuilder().baseUrl(url).apply(block).build()

inline fun <reified T : Annotation> Request.methodAnnotationOf() =
  tag(Invocation::class.java)?.method()?.getAnnotation(T::class.java)