package com.dylanc.retrofit.sample.kotlin.ui

import androidx.lifecycle.asLiveData
import com.dylanc.retrofit.coroutines.RequestViewModel
import com.dylanc.retrofit.coroutines.livedata.catch
import com.dylanc.retrofit.coroutines.livedata.showLoading
import com.dylanc.retrofit.sample.kotlin.data.repository.DataRepository

class CoroutinesViewModel : RequestViewModel() {

  val user = DataRepository.userLiveData

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