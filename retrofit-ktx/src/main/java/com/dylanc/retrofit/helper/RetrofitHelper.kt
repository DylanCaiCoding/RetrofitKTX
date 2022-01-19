@file:Suppress("unused")

package com.dylanc.retrofit.helper

import android.content.Context
import com.dylanc.retrofit.cookiejar.PersistentCookieJarFactory
import com.dylanc.retrofit.cookiejar.persistentCookieJar
import com.dylanc.retrofit.createServiceWithApiUrl
import com.dylanc.retrofit.defaultRetrofit
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
  fun <T> create(service: Class<T>): T =
    defaultRetrofit.createServiceWithApiUrl(service)

  @JvmStatic
  fun putDomain(name: String, url: String) {
    retrofitDomains[name] = url
  }

  @JvmStatic
  fun clearPersistentCookieJar() =
    PersistentCookieJarFactory.clear()

  class Builder {
    private val headers = mutableMapOf<String, String>()
    private val okHttpClientBuilder = OkHttpClient.Builder()
    private var retrofitBuilder = Retrofit.Builder()

    fun migrateFrom(retrofit: Retrofit) {
      retrofitBuilder = retrofit.newBuilder()
    }

    fun baseUrl(baseUrl: String) = apply {
      retrofitBuilder.baseUrl(baseUrl)
    }

    fun putDomain(name: String, url: String) = apply {
      retrofitDomains[name] = url
    }

    fun multipleDomains() = apply {
      okHttpClientBuilder.multipleDomains()
    }

    fun addHeader(name: String, value: String) = apply {
      headers[name] = value
    }

    fun addHeaders(headers: Map<String, String>) = apply {
      this.headers.putAll(headers)
    }

    fun addHeaders(block: (Request) -> Map<String, String>) =
      addInterceptor(object : HeadersInterceptor() {
        override fun onCreateHeaders(request: Request) = block(request)
      })

    fun connectTimeout(connectTimeout: Long, unit: TimeUnit) = apply {
      okHttpClientBuilder.connectTimeout(connectTimeout, unit)
    }

    fun writeTimeout(writeTimeout: Long, unit: TimeUnit) = apply {
      okHttpClientBuilder.connectTimeout(writeTimeout, unit)
    }

    fun readTimeout(readTimeout: Long, unit: TimeUnit) = apply {
      okHttpClientBuilder.connectTimeout(readTimeout, unit)
    }

    fun retryOnConnectionFailure(retryOnConnectionFailure: Boolean) = apply {
      okHttpClientBuilder.retryOnConnectionFailure(retryOnConnectionFailure)
    }

    fun authenticator(authenticator: Authenticator) = apply {
      okHttpClientBuilder.authenticator(authenticator)
    }

    fun cacheControl(
      context: Context,
      maxSize: Long = 10L * 1024 * 1024,
      block: CacheControl.Builder.(Request) -> Unit = {}
    ) = apply {
      okHttpClientBuilder.cacheControl(context, maxSize, block)
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

    fun persistentCookiesJar(context: Context) = apply {
      okHttpClientBuilder.persistentCookieJar(context)
    }

    fun addInterceptor(interceptor: Interceptor) = apply {
      okHttpClientBuilder.addInterceptor(interceptor)
    }

    fun addNetworkInterceptor(interceptor: Interceptor) = apply {
      okHttpClientBuilder.addNetworkInterceptor(interceptor)
    }

    @JvmOverloads
    fun printHttpLog(
      level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
      logger: HttpLoggingInterceptor.Logger
    ) = apply {
      okHttpClientBuilder.printHttpLog(level, logger)
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

    fun okHttpClientBuilder(block: Callback<OkHttpClient.Builder>) = apply {
      block(okHttpClientBuilder)
    }

    fun retrofitBuilder(block: Callback<Retrofit.Builder>) = apply {
      block(retrofitBuilder)
    }

    fun init() =
      initRetrofit(
        retrofitBuilder
          .client(okHttpClientBuilder.addHeaders(headers).build())
          .addConverterFactory(GsonConverterFactory.create())
          .build()
      )
  }

  fun interface Callback<in P> {
    operator fun invoke(p: P)
  }
}