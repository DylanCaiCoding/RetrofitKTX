package com.dylanc.retrofit.helper.sample.data.request

import androidx.lifecycle.asLiveData
import com.dylanc.retrofit.helper.coroutines.*
import com.dylanc.retrofit.helper.sample.data.repository.DataRepository

class TestRequest : ViewModelRequest() {

  fun geArticleList() =
    DataRepository.geArticleList()
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()

  fun getGankTodayList() =
    DataRepository.getGankTodayList()
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()
}