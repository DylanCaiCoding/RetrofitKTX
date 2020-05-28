@file:Suppress("unused")
@file:JvmName("RetrofitHelper")

package com.dylanc.retrofit.helper

import com.dylanc.retrofit.helper.interceptor.DomainsInterceptor
import com.dylanc.retrofit.helper.interceptor.HeaderInterceptor
import okhttp3.Authenticator
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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

val retrofitInitiator: Initiator
  @JvmName("getDefault")
  get() = Initiator.INSTANCE

@JvmName("init")
fun initRetrofit(init: Initiator.() -> Unit = {}) =
  retrofitInitiator.apply(init).init()

@JvmName("create")
fun <T> apiServiceOf(service: Class<T>): T = if (retrofitInitiator.isInitialized) {
  retrofitInitiator.retrofit.create(service)
} else {
  throw NullPointerException("RetrofitHelper is not initialized!")
}

inline fun <reified T> apiServiceOf(): T = apiServiceOf(T::class.java)

class Initiator private constructor() {

  private var debug: Boolean = false
  private val headers = HashMap<String, String>()
  private val debugInterceptors = ArrayList<Interceptor>()
  private val okHttpClientBuilder: OkHttpClient.Builder by lazy { OkHttpClient.Builder() }
  private val retrofitBuilder: Retrofit.Builder by lazy { Retrofit.Builder() }
  internal lateinit var retrofit: Retrofit
    private set

  fun debug(debug: Boolean) = apply {
    this.debug = debug
  }

  fun addHeader(name: String, value: String) = apply {
    headers[name] = value
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
  fun addHttpLoggingInterceptor(
    level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
    log: (message: String) -> Unit
  ) = addHttpLoggingInterceptor(level, object : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
      log(message)
    }
  })

  @JvmOverloads
  fun addHttpLoggingInterceptor(
    level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
    logger: HttpLoggingInterceptor.Logger
  ) = apply {
    addInterceptor(HttpLoggingInterceptor(logger).apply { this.level = level })
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
    val baseUrl = baseUrl ?: throw NullPointerException("Please set the base url by @BaseUrl.")
    val okHttpClient = okHttpClientBuilder
      .apply {
        if (domains.isNotEmpty()) {
          addInterceptor(DomainsInterceptor(DOMAIN, domains))
        }
        if (headers.isNotEmpty()) {
          addInterceptor(HeaderInterceptor(headers))
        }
        if (debug) {
          for (interceptor in debugInterceptors) {
            addInterceptor(interceptor)
          }
        }
      }
      .build()
    retrofit = retrofitBuilder
      .baseUrl(baseUrl)
      .client(okHttpClient)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  internal val isInitialized
    get() = ::retrofit.isInitialized

  private val baseUrl: String?
    get() {
      val debugUrl = urlConfigOf<String>("debugUrl")
      return debugUrl ?: urlConfigOf<String>("baseUrl")
    }

  private val domains: Map<String, String>
    get() {
      val domains = urlConfigOf<Map<String, String>>("domains")
      return domains ?: hashMapOf()
    }

  @Suppress("UNCHECKED_CAST")
  private fun <T> urlConfigOf(fieldName: String): T? = try {
    val clazz = Class.forName("com.dylanc.retrofit.helper.UrlConfig")
    val urlConfig = clazz.newInstance()
    clazz.getField(fieldName)[urlConfig] as T?
  } catch (e: Exception) {
    e.printStackTrace()
    null
  }

  companion object {
    internal val INSTANCE: Initiator by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Initiator() }
  }
}