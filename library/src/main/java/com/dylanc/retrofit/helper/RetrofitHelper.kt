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
inline fun <reified T> RetrofitHelper.create(): T = create(T::class.java)

object RetrofitHelper {

  const val DOMAIN_HEADER = RetrofitUrlManager.DOMAIN_NAME_HEADER

  @JvmStatic
  val default: Default
    get() = Default.INSTANCE

  @JvmStatic
  fun <T> create(service: Class<T>): T {
    return retrofit.create(service)
  }

  fun <T : Any> create(service: KClass<T>): T {
    return retrofit.create(service.java)
  }

  private val retrofit: Retrofit
    get() = default.retrofit

  class Default private constructor() {

    private var baseUrl: String? = null
    private var debugUrl: String? = null
    private var debug: Boolean = false
    private var connectTimeout: Long = 10
    private var retryOnConnectionFailure = false
    private var readTimeout: Long = 10
    private var writeTimeout: Long = 10
    private var progressRefreshTime = 150
    private val headers = HashMap<String, String>()
    private val interceptors = ArrayList<Interceptor>()
    private val domains = HashMap<String, String>()
    private val debugInterceptors = ArrayList<Interceptor>()
    private var sslSocketFactory: SSLSocketFactory? = null
    private var trustManager: X509TrustManager? = null
    private var callAdapterFactories = ArrayList<CallAdapter.Factory>()
    private var converterFactories = ArrayList<Converter.Factory>()
    private var okHttpClientBuilder: OkHttpClient.Builder? = null
    private var retrofitBuilder: Retrofit.Builder? = null
    internal var cookieJar: CookieJar? = null
      private set
    internal var requestLoading: RequestLoading? = null
      private set
    internal lateinit var retrofit: Retrofit
      private set

    companion object {
      val INSTANCE: Default by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Default()
      }
    }

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
      this.retryOnConnectionFailure = retryOnConnectionFailure
    }

    fun progressRefreshTime(progressRefreshTime: Int) = apply {
      this.progressRefreshTime = progressRefreshTime
    }

    fun connectTimeout(connectTimeout: Long) = apply {
      this.connectTimeout = connectTimeout
    }

    fun writeTimeout(writeTimeout: Long) = apply {
      this.writeTimeout = writeTimeout
    }

    fun readTimeout(readTimeout: Long) = apply {
      this.readTimeout = readTimeout
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

    fun requestLoading(requestLoading: RequestLoading) = apply {
      this.requestLoading = requestLoading
    }

    fun okHttpClientBuilder(okHttpClientBuilder: OkHttpClient.Builder) = apply {
      this.okHttpClientBuilder = okHttpClientBuilder
    }

    fun retrofitBuilder(retrofitBuilder: Retrofit.Builder) = apply {
      this.retrofitBuilder = retrofitBuilder
    }

    fun setPersistentCookieJar(context: Context) = apply {
      this.cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    }

    fun addDebugInterceptor(context: Context, debugUrl: String, debugRawId: Int) = apply {
      addInterceptor(DebugInterceptor(context, debugUrl, debugRawId))
    }

    fun addConverterFactory(factory: Converter.Factory) = apply {
      converterFactories.add(factory)
    }

    fun addCallAdapterFactory(factory: CallAdapter.Factory) = apply {
      callAdapterFactories.add(factory)
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
      this.sslSocketFactory = sslSocketFactory
      this.trustManager = trustManager
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
      val okHttpClientBuilder = this.okHttpClientBuilder ?: OkHttpClient.Builder()
      val okHttpClient = okHttpClientBuilder
        .retryOnConnectionFailure(retryOnConnectionFailure)
        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .writeTimeout(writeTimeout, TimeUnit.SECONDS)
        .apply {
          RetrofitUrlManager.getInstance().with(this)
          ProgressManager.getInstance().with(this)
          ProgressManager.getInstance().setRefreshTime(progressRefreshTime)
          if (headers.isNotEmpty()) {
            addInterceptor(HeaderInterceptor(headers))
          }
          for (interceptor in interceptors) {
            addInterceptor(interceptor)
          }
          for ((key, value) in domains) {
            RetrofitUrlManager.getInstance().putDomain(key, value)
          }
          cookieJar?.let { cookieJar(it) }
          sslSocketFactory?.let { sslSocketFactory ->
            trustManager?.let { sslSocketFactory(sslSocketFactory, it) }
          }
        }
        .build()
      val retrofitBuilder = this.retrofitBuilder ?: Retrofit.Builder()
      retrofit = retrofitBuilder
        .baseUrl(checkNotNull(baseUrl))
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .apply {
          for (converterFactory in converterFactories) {
            addConverterFactory(converterFactory)
          }
          for (callAdapterFactory in callAdapterFactories) {
            addCallAdapterFactory(callAdapterFactory)
          }
        }
        .build()
    }
  }

}
