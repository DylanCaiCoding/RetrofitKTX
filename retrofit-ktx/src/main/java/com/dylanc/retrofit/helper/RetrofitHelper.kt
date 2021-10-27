@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper

import com.dylanc.callbacks.Callback1
import com.dylanc.retrofit.annotationBaseUrl
import com.dylanc.retrofit.cookie.PersistentCookieJar
import com.dylanc.retrofit.createServiceWithApiUrl
import com.dylanc.retrofit.initRetrofit
import com.dylanc.retrofit.interceptor.*
import okhttp3.*
import okhttp3.CacheControl
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author Dylan Cai
 */
object RetrofitHelper {

  @JvmStatic
  fun getDefault() = Builder()

  @JvmStatic
  fun putDomain(name: String, url: String) {
    retrofitDomains[name] = url
  }

  @JvmStatic
  fun <T> create(service: Class<T>): T {
    return createServiceWithApiUrl(service)
  }

  class Builder {
    private var debug = false
    private var useNewBaseUrl = true
    private val headers = mutableMapOf<String, String>()
    private val okHttpClientBuilder = OkHttpClient.Builder()
    private var retrofitBuilder = Retrofit.Builder()

    @JvmOverloads
    fun migrateFrom(retrofit: Retrofit, useNewBaseUrl: Boolean = false) {
      retrofitBuilder = retrofit.newBuilder()
      this.useNewBaseUrl = useNewBaseUrl
    }

    fun debug(debug: Boolean) = apply {
      this.debug = debug
    }

    fun addHeader(name: String, value: String) = apply {
      headers[name] = value
    }

    fun addHeaders(vararg pairs: Pair<String, String>) = apply {
      headers.putAll(pairs)
    }

    fun addHeaders(headers: Map<String, String>) = apply {
      this.headers.putAll(headers)
    }

    fun addHeaders(block: (Request) -> Map<String, String>) =
      addInterceptor(object : HeadersInterceptor() {
        override fun onCreateHeaders(request: Request) = block(request)
      })

    fun baseUrl(baseUrl: String) = apply {
      retrofitBuilder.baseUrl(baseUrl)
      useNewBaseUrl = false
    }

    fun putDomain(name: String, url: String) {
      retrofitDomains[name] = url
    }

    @JvmOverloads
    fun connectTimeout(connectTimeout: Long, unit: TimeUnit = TimeUnit.SECONDS) = apply {
      okHttpClientBuilder.connectTimeout(connectTimeout, unit)
    }

    @JvmOverloads
    fun writeTimeout(writeTimeout: Long, unit: TimeUnit = TimeUnit.SECONDS) = apply {
      okHttpClientBuilder.connectTimeout(writeTimeout, unit)
    }

    @JvmOverloads
    fun readTimeout(readTimeout: Long, unit: TimeUnit = TimeUnit.SECONDS) = apply {
      okHttpClientBuilder.connectTimeout(readTimeout, unit)
    }

    fun retryOnConnectionFailure(retryOnConnectionFailure: Boolean) = apply {
      okHttpClientBuilder.retryOnConnectionFailure(retryOnConnectionFailure)
    }

    fun authenticator(authenticator: Authenticator) = apply {
      okHttpClientBuilder.authenticator(authenticator)
    }

    fun cacheControl(
      maxSize: Long = 10L * 1024 * 1024,
      block: CacheControl.Builder.(Request) -> Unit = {}
    ) = apply {
      okHttpClientBuilder.cacheControl(maxSize, block)
    }

    fun cacheControl(
      directory: File,
      maxSize: Long = 10L * 1024 * 1024,
      block: CacheControl.Builder.(Request) -> Unit = {}
    ) = apply {
      okHttpClientBuilder.cacheControl(directory, maxSize, block)
    }

    fun cookieJar(cookieJar: CookieJar) = apply {
      okHttpClientBuilder.cookieJar(cookieJar)
    }

    fun persistentCookies() = cookieJar(PersistentCookieJar())

    fun multipleDomains() = apply {
      okHttpClientBuilder.multipleDomains()
    }

    fun addInterceptor(interceptor: Interceptor) = apply {
      okHttpClientBuilder.addInterceptor(interceptor)
    }

    fun addNetworkInterceptor(interceptor: Interceptor) = apply {
      okHttpClientBuilder.addNetworkInterceptor(interceptor)
    }

    fun addInterceptors(vararg interceptors: Interceptor) = apply {
      for (interceptor in interceptors) {
        okHttpClientBuilder.addInterceptor(interceptor)
      }
    }

    @JvmOverloads
    fun addHttpLog(
      level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
      logger: HttpLoggingInterceptor.Logger
    ) = apply {
      okHttpClientBuilder.addHttpLog(level, logger)
    }

    fun doOnResponse(block: (Response, String, String) -> Response) = apply {
      okHttpClientBuilder.doOnResponse(block)
    }

    fun addConverterFactory(factory: Converter.Factory) = apply {
      retrofitBuilder.addConverterFactory(factory)
    }

    fun addCallAdapterFactory(factory: CallAdapter.Factory) = apply {
      retrofitBuilder.addCallAdapterFactory(factory)
    }

    fun okHttpClientBuilder(block: Callback1<OkHttpClient.Builder>) = apply {
      block(okHttpClientBuilder)
    }

    fun retrofitBuilder(block: Callback1<Retrofit.Builder>) = apply {
      block(retrofitBuilder)
    }

    fun init() {
      val retrofit = retrofitBuilder
        .apply { if (useNewBaseUrl) annotationBaseUrl(debug) }
        .client(
          okHttpClientBuilder
            .addHeaders(headers)
            .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
      initRetrofit(retrofit)
    }
  }
}