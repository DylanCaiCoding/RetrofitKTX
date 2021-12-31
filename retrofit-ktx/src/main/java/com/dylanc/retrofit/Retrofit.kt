@file:Suppress("unused")

package com.dylanc.retrofit

import com.dylanc.retrofit.annotations.ApiUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Invocation
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Dylan Cai
 */

internal lateinit var defaultRetrofit: Retrofit
private val retrofitsCache = mutableMapOf<Class<*>, Retrofit>().withDefault { defaultRetrofit }

inline fun initRetrofit(debug: Boolean = false, crossinline block: Retrofit.Builder.() -> Unit) =
  initRetrofit(retrofit {
    annotatedBaseUrl(debug)
    apply(block)
    addConverterFactory(GsonConverterFactory.create())
  })

inline fun initRetrofit(retrofit: Retrofit, crossinline block: Retrofit.Builder.() -> Unit) =
  initRetrofit(retrofit.copy(block))

fun initRetrofit(retrofit: Retrofit) {
  if (::defaultRetrofit.isInitialized) {
    throw IllegalStateException("Don't allow the default retrofit to reinitialize.")
  }
  defaultRetrofit = retrofit
}

inline fun <reified T> apiServices() = apiServices(T::class.java)

fun <T> apiServices(service: Class<T>) = lazy {
  if (!::defaultRetrofit.isInitialized) {
    throw IllegalStateException("Please initialize default retrofit.")
  }
  defaultRetrofit.createServiceWithApiUrl(service)
}

fun <T> Retrofit.createServiceWithApiUrl(service: Class<T>): T {
  val apiUrl = service.getAnnotation(ApiUrl::class.java)
  if (apiUrl != null && apiUrl.value.isNotEmpty() && retrofitsCache[service] == null) {
    retrofitsCache[service] = this.copy { baseUrl(apiUrl.value) }
  }
  return retrofitsCache.getValue(service).create(service)
}

inline fun retrofit(crossinline block: Retrofit.Builder.() -> Unit): Retrofit =
  Retrofit.Builder().apply(block).build()

inline fun Retrofit.Builder.okHttpClient(crossinline block: OkHttpClient.Builder.() -> Unit): Retrofit.Builder =
  client(OkHttpClient.Builder().apply(block).build())

inline fun Retrofit.copy(crossinline block: Retrofit.Builder.() -> Unit = {}): Retrofit =
  newBuilder().apply(block).build()

inline fun <reified T : Annotation> Request.getMethodAnnotation(): T? =
  tag(Invocation::class.java)?.method()?.getAnnotation(T::class.java)

fun Retrofit.Builder.annotatedBaseUrl(debug: Boolean = false) = apply {
  (if (debug) debugUrl ?: baseUrl else baseUrl)?.let { baseUrl(it) }
}
