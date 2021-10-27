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

private lateinit var defaultRetrofit: Retrofit
private val retrofits = mutableMapOf<String, Retrofit>().withDefault { defaultRetrofit }

inline fun initRetrofit(debug: Boolean = false, crossinline block: Retrofit.Builder.() -> Unit) =
  initRetrofit(
    retrofit {
      annotationBaseUrl(debug)
      apply(block)
      addConverterFactory(GsonConverterFactory.create())
    }
  )

inline fun initRetrofit(retrofit: Retrofit, crossinline block: Retrofit.Builder.() -> Unit) =
  initRetrofit(retrofit.newBuilder().apply(block).build())

fun initRetrofit(retrofit: Retrofit) {
  if (::defaultRetrofit.isInitialized) {
    throw IllegalStateException("Don't allow the default retrofit to reinitialize.")
  }
  defaultRetrofit = retrofit
}

inline fun <reified T> apiServices() = lazy {
  createServiceWithApiUrl(T::class.java)
}

fun <T> createServiceWithApiUrl(service: Class<T>): T {
  if (!::defaultRetrofit.isInitialized) {
    initRetrofit {}
  }
  val apiUrl = service.getAnnotation(ApiUrl::class.java)?.value
  if (apiUrl != null && retrofits[apiUrl] == null) {
    retrofits[apiUrl] = retrofits.getValue(apiUrl).createRetrofit(apiUrl)
  }
  return retrofits.getValue(apiUrl.orEmpty()).create(service)
}

inline fun retrofit(crossinline block: Retrofit.Builder.() -> Unit): Retrofit =
  Retrofit.Builder().apply(block).build()

inline fun Retrofit.Builder.okHttpClient(crossinline block: OkHttpClient.Builder.() -> Unit): Retrofit.Builder =
  client(OkHttpClient.Builder().apply(block).build())

inline fun Retrofit.createRetrofit(url: String, crossinline block: Retrofit.Builder.() -> Unit = {}): Retrofit =
  newBuilder().baseUrl(url).apply(block).build()

inline fun <reified T : Annotation> Request.getMethodAnnotation() =
  tag(Invocation::class.java)?.method()?.getAnnotation(T::class.java)

fun Retrofit.Builder.annotationBaseUrl(debug: Boolean = false) = apply {
  (if (debug) debugUrl ?: baseUrl else baseUrl)?.let { baseUrl(it) }
}
