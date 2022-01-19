package com.dylanc.retrofit.sample.kotlin

import android.app.Application
import android.util.Log
import com.dylanc.retrofit.cookiejar.persistentCookieJar
import com.dylanc.retrofit.coroutines.initRequestViewModel
import com.dylanc.retrofit.initRetrofit
import com.dylanc.retrofit.interceptor.multipleDomains
import com.dylanc.retrofit.interceptor.printHttpLog
import com.dylanc.retrofit.okHttpClient
import com.dylanc.retrofit.sample.kotlin.constant.BASE_URL
import com.dylanc.retrofit.sample.kotlin.constant.URL_GANK
import com.dylanc.retrofit.sample.kotlin.network.LoadingDialogFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

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
        multipleDomains("gank" to URL_GANK)
        printHttpLog { Log.i("http", it) }
        persistentCookieJar(applicationContext)
      }
      addConverterFactory(ScalarsConverterFactory.create())
      addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    }

    initRequestViewModel(LoadingDialogFactory())
  }
}
