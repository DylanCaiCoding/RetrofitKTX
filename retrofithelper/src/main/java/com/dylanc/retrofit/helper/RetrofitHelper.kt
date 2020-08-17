@file:Suppress("unused")
@file:JvmName("RetrofitHelper")

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
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
 * @author Dylan Cai
 */
private const val DOMAIN = "Domain"
const val DOMAIN_HEADER = "$DOMAIN:"

@get:JvmName("getDefault")
val retrofitInitializer: Builder by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Builder() }

@JvmName("init")
fun initRetrofit(init: Builder.() -> Unit) = retrofitInitializer.apply(init).init()

@JvmName("create")
fun <T> apiServiceOf(service: Class<T>): T {
  if (!retrofitInitializer.isInitialized) {
    retrofitInitializer.init()
  }
  return retrofitInitializer.retrofit.apiServiceOf(service)
}

inline fun <reified T> apiServiceOf(): T = apiServiceOf(T::class.java)

fun <T> Retrofit.apiServiceOf(service: Class<T>): T {
  val apiUrl = service.getAnnotation(ApiUrl::class.java)
  return if (apiUrl != null) {
    createRetrofit(apiUrl.value).create(service)
  } else {
    create(service)
  }
}

//inline fun <reified T> Retrofit.create(): T = create(T::class.java)

inline fun <reified T> Retrofit.create(): T =
  if (T::class.java.getAnnotation(ApiUrl::class.java) != null) {
    val apiUrl: ApiUrl = T::class.java.getAnnotation(ApiUrl::class.java)!!
    createRetrofit(apiUrl.value).create(T::class.java)
  } else {
    create(T::class.java)
  }

@JvmOverloads
fun Retrofit.createRetrofit(url: String, block: Retrofit.Builder.() -> Unit = {}): Retrofit =
  newBuilder().baseUrl(url).apply(block).build()

fun putDomain(domain: String, url: String) {
  retrofitInitializer.domains[domain] = url
}

class Builder{

  private var debug: Boolean = false
  private val headers = HashMap<String, String>()
  private val debugInterceptors = ArrayList<Interceptor>()
  private val okHttpClientBuilder: OkHttpClient.Builder by lazy { OkHttpClient.Builder() }
  private val retrofitBuilder: Retrofit.Builder by lazy {
    val baseUrl = baseUrl ?: throw NullPointerException("Please sets the base url by @BaseUrl.")
    Retrofit.Builder().baseUrl(baseUrl)
  }
  internal val domains: MutableMap<String, String> by lazy {
    urlConfigOf<MutableMap<String, String>>("domains") ?: mutableMapOf()
  }
  lateinit var retrofit: Retrofit
    private set

  fun debug(debug: Boolean) = apply {
    this.debug = debug
  }

  fun addHeader(name: String, value: String) = apply {
    headers[name] = value
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

  fun cache(cache: Cache) = apply {
    okHttpClientBuilder.cache(cache)
  }

  fun cacheControl(onCreateCacheControl: () -> CacheControl?) = apply {
    okHttpClientBuilder.cacheControl(onCreateCacheControl)
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

  fun init() {
    val okHttpClient = okHttpClientBuilder
      .putDomains(DOMAIN_HEADER, domains)
      .addHeaders(headers)
      .apply {
        if (debug) {
          for (interceptor in debugInterceptors) {
            addInterceptor(interceptor)
          }
        }
      }
      .build()
    retrofit = retrofitBuilder
      .client(okHttpClient)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  internal val isInitialized
    get() = ::retrofit.isInitialized

  private val baseUrl: String?
    get() = if (debug) {
      urlConfigOf<String>("debugUrl") ?: urlConfigOf<String>("baseUrl")
    } else {
      urlConfigOf<String>("baseUrl")
    }

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
}