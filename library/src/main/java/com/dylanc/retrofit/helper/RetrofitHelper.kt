package com.dylanc.retrofit.helper

import android.content.Context
import android.text.TextUtils
import com.dylanc.retrofit.helper.interceptor.DebugInterceptor
import com.dylanc.retrofit.helper.interceptor.HeaderInterceptor
import io.reactivex.Observable
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
object RetrofitHelper {

  @JvmStatic
  val default: Default
    get() = Default.instance

  @JvmStatic
  fun <T> create(service: Class<T>): T {
    return retrofit.create(service)
  }

  fun <T : Any> create(service: KClass<T>): T {
    return retrofit.create(service.java)
  }

  private val retrofit: Retrofit
    get() = RetrofitHolder.instance.retrofit

  private class RetrofitHolder private constructor(){
    var retrofit: Retrofit
      private set

    companion object {
      val instance: RetrofitHolder by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        RetrofitHolder()
      }
    }

    init {
      retrofit = Retrofit.Builder()
        .baseUrl(RetrofitHelper.default.baseUrl!!)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpHolder.instance.okHttpClient)
        .build()
    }
  }

  private class OkHttpHolder private constructor(){
    private val timeOut = 60
    var okHttpClient: OkHttpClient
      private set

    companion object {
      val instance: OkHttpHolder by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        OkHttpHolder()
      }
    }

    init {
      var builder: OkHttpClient.Builder = OkHttpClient.Builder()
        .retryOnConnectionFailure(false)
        .connectTimeout(timeOut.toLong(), TimeUnit.SECONDS)
      builder = RetrofitUrlManager.getInstance().with(builder)
      builder = ProgressManager.getInstance().with(builder)
      for (interceptor in default.interceptors) {
        builder.addInterceptor(interceptor)
      }
      if (default.headers.isNotEmpty()) {
        builder.addInterceptor(HeaderInterceptor(default.headers))
      }
      default.cookieJar?.let { builder.cookieJar(it) }
      if (default.domains.isNotEmpty()) {
        for ((key, value) in default.domains) {
          RetrofitManager.putDomain(key, value)
        }
      }
      if (default.downloadRefreshTime >= 0) {
        RetrofitManager.setDownloadRefreshTime(default.downloadRefreshTime)
      }
      okHttpClient = builder.build()
    }
  }

  class Default private constructor() {

    var baseUrl: String? = null
      private set
    var downloadRefreshTime: Int = -1
      private set
    var cookieJar: CookieJar? = null
      private set
    internal val headers = HashMap<String, String>()
    internal val interceptors = ArrayList<Interceptor>()
    internal val domains = HashMap<String, String>()
    private var debugUrl: String? = null
    private var debug: Boolean = false
    private val debugInterceptors = ArrayList<Interceptor>()

    companion object {
      val instance: Default by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        Default()
      }
    }

    fun baseUrl(baseUrl: String): Default {
      this.baseUrl = baseUrl
      return this
    }

    fun debugUrl(debugUrl: String): Default {
      this.debugUrl = debugUrl
      return this
    }

    fun debugMode(debug: Boolean): Default {
      this.debug = debug
      return this
    }

    fun downloadRefreshTime(refreshTime: Int): Default {
      downloadRefreshTime = refreshTime
      return this
    }

    fun putDomain(domainName: String, domainUrl: String): Default {
      domains[domainName] = domainUrl
      return this
    }

    fun addHeader(name: String, value: String): Default {
      headers[name] = value
      return this
    }

    fun addDebugInterceptor(context: Context, debugUrl: String, debugRawId: Int): Default {
      return addInterceptor(DebugInterceptor(context, debugUrl, debugRawId))
    }

    fun addLoggingInterceptor(logger: HttpLoggingInterceptor.Logger): Default {
      return addLoggingInterceptor(HttpLoggingInterceptor.Level.BODY, logger)
    }

    fun addLoggingInterceptor(level: HttpLoggingInterceptor.Level, logger: HttpLoggingInterceptor.Logger): Default {
      val loggingInterceptor = HttpLoggingInterceptor(logger)
      loggingInterceptor.level = level
      debugInterceptors.add(loggingInterceptor)
      return this
    }

    fun addInterceptor(interceptor: Interceptor): Default {
      if (interceptor is DebugInterceptor) {
        debugInterceptors.add(interceptor)
      } else {
        interceptors.add(interceptor)
      }
      return this
    }

    fun addInterceptors(interceptors: ArrayList<Interceptor>): Default {
      for (interceptor in interceptors) {
        addInterceptor(interceptor)
      }
      return this
    }

    fun setCookieJar(cookieJar: CookieJar): Default {
      this.cookieJar = cookieJar
      return this
    }

    fun init() {
      if (debug) {
        if (!TextUtils.isEmpty(debugUrl)) {
          baseUrl = debugUrl
        }
        interceptors.addAll(debugInterceptors)
      }
    }
  }

}
