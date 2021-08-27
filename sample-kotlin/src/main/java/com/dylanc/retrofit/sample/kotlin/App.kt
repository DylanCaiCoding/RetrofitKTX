package com.dylanc.retrofit.sample.kotlin

import android.app.Application
import android.util.Log
import com.dylanc.retrofit.initRetrofit
import com.dylanc.retrofit.interceptor.addHttpLog
import com.dylanc.retrofit.interceptor.multipleDomains
import com.dylanc.retrofit.okHttpClient
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * @author Dylan Cai
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    initRetrofit(BuildConfig.DEBUG) {
      addConverterFactory(ScalarsConverterFactory.create())
      addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      okHttpClient {
        multipleDomains()
        addHttpLog {
          Log.i(TAG, it)
        }
      }
    }
  }

  companion object {
    const val TAG = "http"
  }
}