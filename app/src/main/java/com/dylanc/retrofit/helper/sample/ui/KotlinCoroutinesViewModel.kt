package com.dylanc.retrofit.helper.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.coroutines.RequestExceptionHandler
import com.dylanc.retrofit.helper.sample.api.CoroutinesApi
import com.dylanc.retrofit.helper.toFile

class KotlinCoroutinesViewModel : ViewModel() {
  private val requestExceptionHandler = RequestExceptionHandler()
  val requestException = requestExceptionHandler.requestException

  fun geArticleList() = liveData(requestExceptionHandler) {
    emit(apiServiceOf<CoroutinesApi>().geArticleList(0))
  }

  fun getGankTodayList() = liveData(requestExceptionHandler) {
    emit(apiServiceOf<CoroutinesApi>().getGankTodayList())
  }

  fun login() = liveData(requestExceptionHandler) {
    emit(apiServiceOf<CoroutinesApi>().login())
  }

  fun download(url: String, path: String) = liveData(requestExceptionHandler) {
    emit(apiServiceOf<CoroutinesApi>().download(url).toFile(path))
  }
}