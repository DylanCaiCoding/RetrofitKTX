@file:Suppress("unused")

package com.dylanc.retrofit.interceptor

import android.content.Context
import com.dylanc.retrofit.getMethodAnnotation
import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit

inline fun cacheControl(crossinline block: okhttp3.CacheControl.Builder.() -> Unit): okhttp3.CacheControl =
  okhttp3.CacheControl.Builder().apply(block).build()

inline fun OkHttpClient.Builder.cacheControl(
  context: Context,
  maxSize: Long = 10L * 1024 * 1024,
  crossinline block: okhttp3.CacheControl.Builder.(Request) -> Unit = {}
) =
  cacheControl(File(context.externalCacheDir, "responses.cache"), maxSize, block)

inline fun OkHttpClient.Builder.cacheControl(
  directory: File,
  maxSize: Long = 10L * 1024 * 1024,
  crossinline block: okhttp3.CacheControl.Builder.(Request) -> Unit = {}
) =
  apply {
    cache(Cache(directory, maxSize))
    addNetworkInterceptor(CacheControlInterceptor {
      cacheControl { block(it) }
    })
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

class CacheControlInterceptor constructor(
  private val onCreateCacheControl: (Request) -> okhttp3.CacheControl?
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val cacheControl = request.getMethodAnnotation<CacheControl>()?.let {
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
      .apply {
        if (cacheControl != null && cacheControl.toString().isNotEmpty()) {
          removeHeader("Pragma")
          removeHeader("Cache-Control")
          header("Cache-Control", cacheControl.toString())
        }
      }
      .build()
  }
}