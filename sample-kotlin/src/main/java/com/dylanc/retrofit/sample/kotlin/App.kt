package com.dylanc.retrofit.sample.kotlin

import android.app.Application
import android.util.Log
import com.dylanc.retrofit.cookie.persistentCookieJar
import com.dylanc.retrofit.coroutines.initRequestViewModel
import com.dylanc.retrofit.initRetrofit
import com.dylanc.retrofit.interceptor.multipleDomains
import com.dylanc.retrofit.interceptor.printHttpLog
import com.dylanc.retrofit.okHttpClient
import com.dylanc.retrofit.sample.kotlin.data.constant.BASE_URL
import com.dylanc.retrofit.sample.kotlin.network.LoadingDialogFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Dylan Cai
 */
@Suppress("unused")
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    initRetrofit {
      baseUrl(BASE_URL)
      okHttpClient {
        multipleDomains()
        printHttpLog { Log.i("http", it) }
        persistentCookieJar()
        connectTimeout(15, TimeUnit.SECONDS)
      }
      addConverterFactory(ScalarsConverterFactory.create())
      addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    initRequestViewModel(LoadingDialogFactory())
  }
}
