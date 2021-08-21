@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper

import com.dylanc.callbacks.Callback1
import com.dylanc.retrofit.helper.annotations.ApiUrl
import com.dylanc.retrofit.helper.cookie.PersistentCookieJar
import com.dylanc.retrofit.helper.interceptor.*
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

inline fun initRetrofit(init: RetrofitHelper.Builder.() -> Unit) =
  RetrofitHelper.defaultBuilder.apply(init).init()

inline fun <reified T> apiServices(retrofitHelper: RetrofitHelper = RetrofitHelper.INSTANCE) =
  lazy { RetrofitHelper.create(T::class.java, retrofitHelper) }

inline val retrofitDomains: MutableMap<String, String> get() = RetrofitHelper.INSTANCE.domains

inline fun RetrofitHelper.Builder.addHeaders(crossinline block: (Request) -> Map<String, String>) =
  addInterceptor(object : HeadersInterceptor() {
    override fun onCreateHeaders(request: Request) = block(request)
  })

class RetrofitHelper private constructor(
  retrofit: Retrofit,
  val domains: MutableMap<String, String>
) {
  private val retrofits = mutableMapOf<String, Retrofit>().withDefault { retrofit }

  companion object {
    @JvmStatic
    @get:JvmName("getDefault")
    val defaultBuilder: Builder = Builder()

    private var defaultRetrofitHelper: RetrofitHelper? = null
    val INSTANCE: RetrofitHelper by lazy {
      if (defaultRetrofitHelper == null) defaultBuilder.init()
      defaultRetrofitHelper!!
    }

    @JvmStatic
    @JvmOverloads
    fun putDomain(name: String, url: String, retrofitHelper: RetrofitHelper = INSTANCE) {
      retrofitHelper.domains[name] = url
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
    private var debug = false
    private var useNewBaseUrl = true
    private val headers = mutableMapOf<String, String>()
    private val debugInterceptors = mutableListOf<Interceptor>()
    private val okHttpClientBuilder = OkHttpClient.Builder()
    private var retrofitBuilder = Retrofit.Builder()
    private val domains by domains()

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

    fun addHeaders(block: MutableMap<String, String>.(Request) -> Unit) = apply {
      okHttpClientBuilder.addHeaders(block)
    }

    fun baseUrl(baseUrl: String) = apply {
      retrofitBuilder.baseUrl(baseUrl)
      useNewBaseUrl = false
    }

    fun putDomain(name: String, url: String) {
      domains[name] = url
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
      maxSize: Long = 10 * 1024 * 1024,
      block: CacheControl.Builder.(Request) -> Unit = {}
    ) = apply {
      okHttpClientBuilder.cacheControl(maxSize, block)
    }

    fun cacheControl(
      directory: File,
      maxSize: Long = 10 * 1024 * 1024,
      block: CacheControl.Builder.(Request) -> Unit = {}
    ) = apply {
      okHttpClientBuilder.cacheControl(directory, maxSize, block)
    }

    fun cookieJar(cookieJar: CookieJar) = apply {
      okHttpClientBuilder.cookieJar(cookieJar)
    }

    fun persistentCookies() = cookieJar(PersistentCookieJar())

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

    fun okHttpClientBuilder(block: Callback1<OkHttpClient.Builder>) = apply {
      block(okHttpClientBuilder)
    }

    fun retrofitBuilder(block: Retrofit.Builder.() -> Unit) = apply {
      retrofitBuilder.apply(block)
    }

    fun retrofitBuilder(block: Callback1<Retrofit.Builder>) = apply {
      block(retrofitBuilder)
    }

    fun build(): RetrofitHelper {
      val okHttpClient = okHttpClientBuilder
        .addHeaders(headers)
        .putDomains(domains)
        .addDebugInterceptors()
        .build()
      val retrofit = retrofitBuilder
        .apply { if (useNewBaseUrl) baseUrl(baseUrlOrDebugUrl) }
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
      return RetrofitHelper(retrofit, domains)
    }

    fun init() {
      if (defaultRetrofitHelper == null) {
        defaultRetrofitHelper = build()
      } else {
        throw IllegalStateException("Don't allow the default retrofit to reinitialize.")
      }
    }

    private fun OkHttpClient.Builder.addDebugInterceptors() = apply {
      if (debug) {
        for (interceptor in debugInterceptors) {
          addInterceptor(interceptor)
        }
      }
    }

    private val baseUrlOrDebugUrl: String
      get() = if (debug) {
        debugUrl ?: baseUrl
      } else {
        baseUrl
      } ?: throw NullPointerException("Please sets the base url by @BaseUrl.")
  }
}