@file:Suppress("unused")

package com.dylanc.retrofit

import com.hjq.gson.factory.GsonFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal lateinit var defaultRetrofit: Retrofit
private val retrofitsCache = mutableMapOf<Class<*>, Retrofit>()

inline fun initRetrofit(crossinline block: Retrofit.Builder.() -> Unit) =
  initRetrofit(retrofit {
    apply(block).addConverterFactory(GsonConverterFactory.create(GsonFactory.getSingletonGson()))
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
  defaultRetrofit.create(service, useApiUrl = true)
}

inline fun <reified T> Retrofit.create(useApiUrl: Boolean): T =
  create(T::class.java, useApiUrl)

fun <T> Retrofit.create(service: Class<T>, useApiUrl: Boolean): T =
  if (useApiUrl) {
    val apiUrl = service.getAnnotation(ApiUrl::class.java)
    if (apiUrl != null && apiUrl.value.isNotEmpty()) {
      retrofitsCache.getOrPut(Retrofit::class.java) { this.copy { baseUrl(apiUrl.value) } }
    } else {
      this
    }
  } else {
    this
  }.create(service)

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ApiUrl(val value: String)
