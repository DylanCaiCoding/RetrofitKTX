package com.dylanc.retrofit.helper.sample.ui

import androidx.lifecycle.*
import com.dylanc.retrofit.helper.coroutines.*
import com.dylanc.retrofit.helper.sample.repository.DataRepository

class KotlinCoroutinesViewModel : ViewModel() {

  val loading = LoadingLiveData()
  val exception = ExceptionLiveData()

  fun geArticleList() =
    DataRepository.geArticleList()
      .showLoading(loading)
      .catch(exception)
      .asLiveData()

  fun getGankTodayList() =
    DataRepository.getGankTodayList()
      .showLoading(loading)
      .catch(exception)
      .asLiveData()

  fun login() =
    DataRepository.login()
      .showLoading(loading)
      .catch(exception)
      .asLiveData()

  fun download(url: String, path: String) =
    DataRepository.download(url, path)
      .showLoading(loading)
      .catch(exception)
      .asLiveData()
}