package com.dylanc.retrofit.helper.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dylanc.retrofit.helper.coroutines.*
import com.dylanc.retrofit.helper.sample.network.DataRepository

class KotlinCoroutinesViewModel : ViewModel() {
  private val requestExceptionHandler = RequestExceptionHandler()
  val requestException = requestExceptionHandler.requestException

  fun geArticleList() = liveData(requestExceptionHandler) {
    emit(DataRepository.geArticleList())
  }

  fun getGankTodayList() = liveData(requestExceptionHandler) {
    emit(DataRepository.getGankTodayList())
  }

  fun login() = requestStateLiveData {
    emit(DataRepository.login())
  }

  fun download(url: String, path: String) = liveData(requestExceptionHandler) {
    emit(DataRepository.download(url, path))
  }
}