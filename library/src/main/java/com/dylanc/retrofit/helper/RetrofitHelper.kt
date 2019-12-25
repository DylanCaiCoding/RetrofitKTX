@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.dylanc.retrofit.helper

import android.content.Context
import android.text.TextUtils
import com.dylanc.retrofit.helper.interceptor.DebugInterceptor
import com.dylanc.retrofit.helper.interceptor.HeaderInterceptor
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.JvmOverloads
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.reflect.KClass

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
inline fun <reified T> apiServiceOf(): T = RetrofitHelper.create(T::class.java)

fun downloadServiceOf(): DownloadService = apiServiceOf()

object RetrofitHelper {

  const val DOMAIN_HEADER = RetrofitUrlManager.DOMAIN_NAME_HEADER

  @JvmStatic
  fun getDefault() = Default.INSTANCE

  @JvmStatic
  fun <T> create(service: Class<T>): T = if (getDefault().isInitialized) {
    getDefault().retrofit.create(service)
  } else {
    throw NullPointerException("RetrofitHelper is not initialized!")
  }

  @JvmStatic
  fun setBaseUrl(baseUrl: String) = RetrofitUrlManager.getInstance().setGlobalDomain(baseUrl)

  @JvmStatic
  fun clearPersistentCookieJar() {
    val cookieJar = getDefault().cookieJar
    if (cookieJar is PersistentCookieJar) {
      cookieJar.clear()
    }
  }

  class Default private constructor() {

    companion object {
      internal val INSTANCE: Default by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { Default() }
    }

    private var baseUrl: String? = null
    private var debugUrl: String? = null
    private var debug: Boolean = false
    private var progressRefreshTime = 150
    private val headers = HashMap<String, String>()
    private val interceptors = ArrayList<Interceptor>()
    private val domains = HashMap<String, String>()
    private val debugInterceptors = ArrayList<Interceptor>()
    private val okHttpClientBuilder: OkHttpClient.Builder by lazy { OkHttpClient.Builder() }
    private val retrofitBuilder: Retrofit.Builder by lazy { Retrofit.Builder() }
    internal var cookieJar: CookieJar? = null
      private set
    internal lateinit var retrofit: Retrofit
      private set
    internal val isInitialized get() = ::retrofit.isInitialized

    fun baseUrl(baseUrl: String) = apply {
      this.baseUrl = baseUrl
    }

    fun debugUrl(debugUrl: String) = apply {
      this.debugUrl = debugUrl
    }

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

    fun progressRefreshTime(progressRefreshTime: Int) = apply {
      this.progressRefreshTime = progressRefreshTime
    }

    fun putDomain(domainName: String, domainUrl: String) = apply {
      domains[domainName] = domainUrl
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

    fun setPersistentCookieJar(context: Context) = apply {
      cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
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
        if (!TextUtils.isEmpty(debugUrl)) {
          baseUrl = debugUrl
        }
        interceptors.addAll(debugInterceptors)
      }
      val okHttpClient = okHttpClientBuilder
        .apply {
          RetrofitUrlManager.getInstance().with(this)
          ProgressManager.getInstance().with(this)
          ProgressManager.getInstance().setRefreshTime(progressRefreshTime)
          for ((key, value) in domains) {
            RetrofitUrlManager.getInstance().putDomain(key, value)
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
        .baseUrl(checkNotNull(baseUrl))
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
    }
  }

}
