@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper

import com.dylanc.retrofit.helper.annotations.ApiUrl
import com.dylanc.retrofit.helper.interceptor.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
 * @author Dylan Cai
 */

private const val NO_BASE_URL = "Please sets the base url by @BaseUrl."
private val baseUrl get() = urlConfigOf<String>("baseUrl")

inline fun initRetrofit(init: RetrofitHelper.Builder.() -> Unit) = RetrofitHelper.defaultBuilder.apply(init).init()

inline fun <reified T> apiServiceOf(retrofitHelper: RetrofitHelper = RetrofitHelper.INSTANCE): T =
  RetrofitHelper.create(T::class.java, retrofitHelper)

fun retrofit(block: Retrofit.Builder.() -> Unit): Retrofit =
  Retrofit.Builder().baseUrl(baseUrl ?: throw NullPointerException(NO_BASE_URL)).apply(block).build()

inline fun Retrofit.Builder.okHttpClient(block: OkHttpClient.Builder.() -> Unit): Retrofit.Builder =
  client(OkHttpClient.Builder().apply(block).build())

inline fun Retrofit.createRetrofit(url: String, block: Retrofit.Builder.() -> Unit = {}): Retrofit =
  newBuilder().baseUrl(url).apply(block).build()

@Suppress("UNCHECKED_CAST")
private fun <T> urlConfigOf(fieldName: String): T? = try {
  val clazz = Class.forName("com.dylanc.retrofit.helper.UrlConfig")
  val urlConfig = clazz.newInstance()
  clazz.getField(fieldName)[urlConfig] as T?
} catch (e: NoSuchFieldException) {
  null
} catch (e: Exception) {
  e.printStackTrace()
  null
}

class RetrofitHelper private constructor(retrofit: Retrofit) {
  private val retrofits = mutableMapOf<String, Retrofit>().withDefault { retrofit }

  companion object {
    @JvmStatic
    @get:JvmName("getDefault")
    val defaultBuilder: Builder = Builder()

    private var defaultRetrofitHelper: RetrofitHelper? = null
    val INSTANCE: RetrofitHelper by lazy {
      defaultRetrofitHelper ?: defaultBuilder.build()
    }

    @JvmStatic
    @JvmOverloads
    fun <T> create(service: Class<T>, retrofitHelper: RetrofitHelper = INSTANCE): T {
      val apiUrl = service.getAnnotation(ApiUrl::class.java)?.value
      val retrofits = retrofitHelper.retrofits
      if (apiUrl != null && retrofits[apiUrl] == null) {
        retrofits[apiUrl] = retrofits.getValue("").createRetrofit(apiUrl)
      }
      return retrofits.getValue(apiUrl ?: "").create(service)
    }
  }

  class Builder {
    private var debug: Boolean = false
    private val headers = HashMap<String, String>()
    private val debugInterceptors = ArrayList<Interceptor>()
    private val okHttpClientBuilder: OkHttpClient.Builder by lazy { OkHttpClient.Builder() }
    private val retrofitBuilder: Retrofit.Builder by lazy {
      Retrofit.Builder().baseUrl(baseUrlOrDebugUrl ?: throw NullPointerException(NO_BASE_URL))
    }

    fun debug(debug: Boolean) = apply {
      this.debug = debug
    }

    fun addHeader(name: String, value: String) = apply {
      headers[name] = value
    }

    fun addHeader(pair: Pair<String, String>) = apply {
      headers[pair.first] = pair.second
    }

    fun baseUrl(baseUrl: String) = apply {
      retrofitBuilder.baseUrl(baseUrl)
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

    fun cache(directory: File, maxSize: Long, onCreateCacheControl: () -> CacheControl?) = apply {
      okHttpClientBuilder.cache(directory, maxSize, onCreateCacheControl)
    }

    fun cookieJar(cookieJar: CookieJar) = apply {
      okHttpClientBuilder.cookieJar(cookieJar)
    }

    @JvmOverloads
    fun addInterceptor(interceptor: Interceptor, debug: Boolean = false) = apply {
      if (interceptor is HttpLoggingInterceptor || debug) {
        debugInterceptors.add(interceptor)
      } else {
        okHttpClientBuilder.addInterceptor(interceptor)
      }
    }

    fun addNetworkInterceptor(interceptor: Interceptor) = apply {
      okHttpClientBuilder.addNetworkInterceptor(interceptor)
    }

    fun addInterceptors(vararg interceptors: Interceptor, debug: Boolean = false) = apply {
      for (interceptor in interceptors) {
        addInterceptor(interceptor, debug)
      }
    }

    @JvmOverloads
    fun addHttpLog(
      level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
      logger: (message: String) -> Unit
    ) = apply {
      okHttpClientBuilder.addHttpLog(level, logger)
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

    fun okHttpClientBuilder(block: OkHttpClient.Builder.() -> Unit) = apply {
      okHttpClientBuilder.apply(block)
    }

    fun retrofitBuilder(block: Retrofit.Builder.() -> Unit) = apply {
      retrofitBuilder.apply(block)
    }

    fun build(): RetrofitHelper {
      val okHttpClient = okHttpClientBuilder
        .addHeaders(headers)
        .addDebugInterceptors()
        .build()
      val retrofit = retrofitBuilder
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
      return RetrofitHelper(retrofit)
    }

    fun init() {
      if (defaultRetrofitHelper == null) {
        defaultRetrofitHelper = build()
      }
    }

    private fun OkHttpClient.Builder.addDebugInterceptors() = apply {
      if (debug) {
        for (interceptor in debugInterceptors) {
          addInterceptor(interceptor)
        }
      }
    }

    private val baseUrlOrDebugUrl: String?
      get() = if (debug) {
        urlConfigOf<String>("debugUrl") ?: urlConfigOf<String>("baseUrl")
      } else {
        urlConfigOf<String>("baseUrl")
      }
  }
}