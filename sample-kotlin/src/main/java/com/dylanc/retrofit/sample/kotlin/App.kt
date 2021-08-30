package com.dylanc.retrofit.sample.kotlin

import android.app.Application
import android.util.Log
import com.dylanc.retrofit.coroutines.initRequestViewModel
import com.dylanc.retrofit.initRetrofit
import com.dylanc.retrofit.interceptor.addHttpLog
import com.dylanc.retrofit.interceptor.multipleDomains
import com.dylanc.retrofit.okHttpClient
import com.dylanc.retrofit.sample.kotlin.network.LoadingDialogFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * @author Dylan Cai
 */
@Suppress("unused")
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    initRetrofit(BuildConfig.DEBUG) {
      okHttpClient {
        multipleDomains()
        addHttpLog { Log.i(TAG, it) }
      }
      addConverterFactory(ScalarsConverterFactory.create())
      addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }

    initRequestViewModel(LoadingDialogFactory())
  }

  companion object {
    const val TAG = "http"
  }
}
