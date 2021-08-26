@file:Suppress("unused")

package com.dylanc.retrofit.helper

import com.dylanc.retrofit.helper.annotations.ApiUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Dylan Cai
 */

lateinit var defaultRetrofit: Retrofit
  internal set

private val retrofits = mutableMapOf<String, Retrofit>().withDefault { defaultRetrofit }

fun initRetrofit(debug: Boolean = false, block: Retrofit.Builder.() -> Unit) =
  initRetrofit(
    Retrofit.Builder().baseUrl(debug).apply(block)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  )

fun initRetrofit(retrofit: Retrofit, block: Retrofit.Builder.() -> Unit) =
  initRetrofit(retrofit.newBuilder().apply(block).build())

fun initRetrofit(retrofit: Retrofit) {
  if (::defaultRetrofit.isInitialized) {
    throw IllegalStateException("Don't allow the default retrofit to reinitialize.")
  }
  defaultRetrofit = retrofit
}

inline fun <reified T> apiServices() = lazy {
  createRetrofitServiceWithApiUrl(T::class.java)
}

fun <T> createRetrofitServiceWithApiUrl(service: Class<T>): T {
  if (!::defaultRetrofit.isInitialized) {
    initRetrofit {}
  }
  val apiUrl = service.getAnnotation(ApiUrl::class.java)?.value
  if (apiUrl != null && retrofits[apiUrl] == null) {
    retrofits[apiUrl] = retrofits.getValue(apiUrl).createRetrofit(apiUrl)
  }
  return retrofits.getValue(apiUrl.orEmpty()).create(service)
}

inline fun retrofit(block: Retrofit.Builder.() -> Unit): Retrofit =
  Retrofit.Builder().apply(block).build()

inline fun Retrofit.Builder.okHttpClient(block: OkHttpClient.Builder.() -> Unit): Retrofit.Builder =
  client(OkHttpClient.Builder().apply(block).build())

inline fun Retrofit.createRetrofit(url: String, block: Retrofit.Builder.() -> Unit = {}): Retrofit =
  newBuilder().baseUrl(url).apply(block).build()

inline fun <reified T : Annotation> Request.methodAnnotationOf() =
  tag(Invocation::class.java)?.method()?.getAnnotation(T::class.java)

internal fun Retrofit.Builder.baseUrl(debug: Boolean) = apply {
  if (debug) {
    debugUrl ?: baseUrl
  } else {
    baseUrl
  }?.let { baseUrl(it) }
}