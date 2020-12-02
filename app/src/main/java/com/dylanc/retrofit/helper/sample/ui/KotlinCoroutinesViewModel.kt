package com.dylanc.retrofit.helper.sample.ui

import androidx.lifecycle.*
import com.dylanc.retrofit.helper.coroutines.*
import com.dylanc.retrofit.helper.sample.data.repository.DataRepository
import com.dylanc.retrofit.helper.sample.data.request.WanAndroidRequest

class KotlinCoroutinesViewModel : RequestViewModel() {

  val wanandroidRequest: WanAndroidRequest by requests()

//  fun geArticleList() =
//    DataRepository.geArticleList()
//      .showLoading(isLoading)
//      .catch(exception)
//      .asLiveData()

  fun getGankTodayList() =
    DataRepository.getGankTodayList()
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun login() =
    DataRepository.login()
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun download(url: String, path: String) =
    DataRepository.download(url, path)
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()
}