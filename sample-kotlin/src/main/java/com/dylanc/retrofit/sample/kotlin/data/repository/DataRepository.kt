package com.dylanc.retrofit.sample.kotlin.data.repository

import androidx.lifecycle.MutableLiveData
import com.dylanc.retrofit.apiServices
import com.dylanc.retrofit.body.toFile
import com.dylanc.retrofit.coroutines.DownloadApi
import com.dylanc.retrofit.sample.kotlin.data.api.CoroutinesApi
import com.dylanc.retrofit.sample.kotlin.data.api.GankApi
import kotlinx.coroutines.flow.flow

object DataRepository {

  private val coroutinesApi: CoroutinesApi by apiServices()
  private val gankApi: GankApi by apiServices()
  private val downloadApi: DownloadApi by apiServices()

  fun geArticleList() = flow {
    emit(coroutinesApi.geArticleList(0))
  }

  fun getGankTodayList() = flow {
    emit(gankApi.getGankTodayListByCoroutines())
  }

  fun login() = flow {
    emit(coroutinesApi.login())
  }

  fun download(url: String, path: String) = flow {
    emit(downloadApi.download(url).toFile(path))
  }

}