@file:Suppress("unused")

package com.dylanc.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal lateinit var defaultRetrofit: Retrofit
private val retrofitsCache = mutableMapOf<Class<*>, Retrofit>().withDefault { defaultRetrofit }

inline fun initRetrofit(crossinline block: Retrofit.Builder.() -> Unit) =
  initRetrofit(retrofit {
    apply(block).addConverterFactory(GsonConverterFactory.create())
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

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ApiUrl(val value: String)