package com.dylanc.retrofit.helper.sample.kotlin.data.repository

import androidx.lifecycle.MutableLiveData
import com.dylanc.retrofit.helper.apiServices
import com.dylanc.retrofit.helper.body.toFile
import com.dylanc.retrofit.helper.coroutines.DownloadApi
import com.dylanc.retrofit.helper.sample.kotlin.data.api.CoroutinesApi
import com.dylanc.retrofit.helper.sample.kotlin.data.api.GankApi
import kotlinx.coroutines.flow.flow

object DataRepository {

  private val coroutinesApi: CoroutinesApi by apiServices()
  private val gankApi: GankApi by apiServices()
  private val downloadApi: DownloadApi by apiServices()

  val userLiveData = MutableLiveData<String>()

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