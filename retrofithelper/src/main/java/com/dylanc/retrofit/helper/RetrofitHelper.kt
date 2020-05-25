@file:Suppress("MemberVisibilityCanBePrivate", "unused")
@file:JvmName("RetrofitHelper")

package com.dylanc.retrofit.helper

import android.content.Context
import com.dylanc.retrofit.helper.interceptor.DebugInterceptor
import com.dylanc.retrofit.helper.interceptor.DomainsInterceptor
import com.dylanc.retrofit.helper.interceptor.HeaderInterceptor
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
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.collections.HashMap

/**
 * @author Dylan Cai
 */
internal const val DOMAIN = "Domain"
const val DOMAIN_HEADER = "$DOMAIN:"

val default: Default
  get() = Default.INSTANCE

@JvmName("init")
fun initRetrofit(init: Default.() -> Unit = {}) =
  default.apply(init).init()

@JvmName("create")
fun <T> apiServiceOf(service: Class<T>): T = if (default.isInitialized) {
  default.retrofit.create(service)
} else {
  throw NullPointerException("RetrofitHelper is not initialized!")
}

inline fun <reified T> apiServiceOf(): T = apiServiceOf(T::class.java)

class Default private constructor() {

  companion object {
    internal val INSTANCE: Default by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Default() }
  }

  private var debug: Boolean = false
  private val headers = HashMap<String, String>()
  private val interceptors = ArrayList<Interceptor>()
  private val debugInterceptors = ArrayList<Interceptor>()
  private val okHttpClientBuilder: OkHttpClient.Builder by lazy { OkHttpClient.Builder() }
  private val retrofitBuilder: Retrofit.Builder by lazy { Retrofit.Builder() }
  private var cookieJar: CookieJar? = null
  internal lateinit var retrofit: Retrofit
    private set
  internal val isInitialized
    get() = ::retrofit.isInitialized

  fun setDebug(debug: Boolean) = apply {
    this.debug = debug
  }

  fun retryOnConnectionFailure(retryOnConnectionFailure: Boolean) = apply {
    okHttpClientBuilder.retryOnConnectionFailure(retryOnConnectionFailure)
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

  fun addHeader(name: String, value: String) = apply {
    headers[name] = value
  }

  fun cookieJar(cookieJar: CookieJar) = apply {
    this.cookieJar = cookieJar
  }

  fun okHttpClientBuilder(block: OkHttpClient.Builder.() -> Unit) = apply {
    okHttpClientBuilder.apply(block)
  }

  fun retrofitBuilder(block: Retrofit.Builder.() -> Unit) = apply {
    retrofitBuilder.apply(block)
  }

  fun addDebugInterceptor(context: Context, debugUrl: String, debugRawId: Int) = apply {
    addInterceptor(DebugInterceptor(context, debugUrl, debugRawId))
  }

  fun addConverterFactory(factory: Converter.Factory) = apply {
    retrofitBuilder.addConverterFactory(factory)
  }

  fun addCallAdapterFactory(factory: CallAdapter.Factory) = apply {
    retrofitBuilder.addCallAdapterFactory(factory)
  }

  @JvmOverloads
  fun addHttpLoggingInterceptor(
    level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
    logger: (message: String) -> Unit
  ) = addHttpLoggingInterceptor(level, object : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
      logger(message)
    }
  })

  @JvmOverloads
  fun addHttpLoggingInterceptor(
    level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
    logger: HttpLoggingInterceptor.Logger
  ) = apply {
    val loggingInterceptor = HttpLoggingInterceptor(logger).apply { this.level = level }
    addInterceptor(loggingInterceptor)
  }

  fun sslSocketFactory(
    sslSocketFactory: SSLSocketFactory,
    trustManager: X509TrustManager
  ) = apply {
    okHttpClientBuilder.sslSocketFactory(sslSocketFactory, trustManager)
  }

  fun addInterceptor(interceptor: Interceptor) = apply {
    if (interceptor is DebugInterceptor || interceptor is HttpLoggingInterceptor) {
      debugInterceptors.add(interceptor)
    } else {
      interceptors.add(interceptor)
    }
  }

  fun addInterceptors(interceptors: ArrayList<Interceptor>) = apply {
    for (interceptor in interceptors) {
      addInterceptor(interceptor)
    }
  }

  fun init() {
    if (debug) {
      interceptors.addAll(debugInterceptors)
    }
    val okHttpClient = okHttpClientBuilder
      .apply {
        if (domains.isNotEmpty()) {
          addInterceptor(DomainsInterceptor(DOMAIN, domains))
        }
        if (headers.isNotEmpty()) {
          addInterceptor(HeaderInterceptor(headers))
        }
        for (interceptor in interceptors) {
          addInterceptor(interceptor)
        }
        cookieJar?.let { cookieJar(it) }
      }
      .build()
    retrofit = retrofitBuilder
      .baseUrl(baseUrl)
      .client(okHttpClient)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  private val baseUrl: String
    get() = domainConfigValueOf("baseUrl") as String

  @Suppress("UNCHECKED_CAST")
  private val domains: HashMap<String, String>
    get() = domainConfigValueOf("domains") as HashMap<String, String>

  fun domainConfigValueOf(fieldName: String): Any? {
    try {
      val clazz = Class.forName("com.dylanc.retrofit.helper.DomainConfig")
      val domainConfig = clazz.newInstance()
      return clazz.getField(fieldName)[domainConfig]
    } catch (e: ClassNotFoundException) {
      e.printStackTrace()
    } catch (e: IllegalAccessException) {
      e.printStackTrace()
    } catch (e: InstantiationException) {
      e.printStackTrace()
    } catch (e: NoSuchFieldException) {
      e.printStackTrace()
    }
    return null
  }
}