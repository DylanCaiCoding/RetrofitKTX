@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.interceptor

import com.dylanc.retrofit.helper.application
import com.dylanc.retrofit.helper.methodAnnotationOf
import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit


inline fun cacheControl(noinline block: okhttp3.CacheControl.Builder.() -> Unit): okhttp3.CacheControl =
  okhttp3.CacheControl.Builder().apply(block).build()

fun OkHttpClient.Builder.cache(
  maxSize: Long = 10 * 1024 * 1024,
  onCreateCacheControl: (Request) -> okhttp3.CacheControl?
) =
  cache(File(application.externalCacheDir, "responses"), maxSize, onCreateCacheControl)

inline fun OkHttpClient.Builder.cache(
  directory: File,
  maxSize: Long = 10 * 1024 * 1024,
  noinline onCreateCacheControl: (Request) -> okhttp3.CacheControl?
) =
  apply {
    cache(Cache(directory, maxSize))
    addNetworkInterceptor(CacheControlInterceptor(onCreateCacheControl))
  }

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class CacheControl(
  val noCache: Boolean = false,
  val noStore: Boolean = false,
  val maxAge: Int = -1,
  val maxStale: Int = -1,
  val minFresh: Int = -1,
  val onlyIfCached: Boolean = false,
  val noTransform: Boolean = false,
  val immutable: Boolean = false,
  val timeUnit: TimeUnit = TimeUnit.SECONDS
)

class CacheControlInterceptor(
  private val onCreateCacheControl: (Request) -> okhttp3.CacheControl?
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val cacheControl = request.methodAnnotationOf<CacheControl>()?.let {
      cacheControl {
        if (it.noCache) noCache()
        if (it.noStore) noStore()
        if (it.maxAge >= 0) maxAge(it.maxAge, it.timeUnit)
        if (it.maxStale >= 0) maxStale(it.maxStale, it.timeUnit)
        if (it.minFresh >= 0) minFresh(it.minFresh, it.timeUnit)
        if (it.onlyIfCached) onlyIfCached()
        if (it.noTransform) noTransform()
        if (it.immutable) immutable()
      }
    } ?: onCreateCacheControl(request)
    return chain.proceed(request).newBuilder()
      .removeHeader("Pragma")
      .apply {
        if (cacheControl != null) {
          removeHeader("Cache-Control")
          header("Cache-Control", cacheControl.toString())
        }
      }
      .build()
  }
}