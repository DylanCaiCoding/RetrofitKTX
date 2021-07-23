package com.dylanc.retrofit.helper.sample.ui

import androidx.lifecycle.asLiveData
import com.dylanc.retrofit.helper.coroutines.RequestViewModel
import com.dylanc.retrofit.helper.coroutines.livedata.catch
import com.dylanc.retrofit.helper.coroutines.livedata.showLoading
import com.dylanc.retrofit.helper.sample.data.repository.DataRepository

class KotlinCoroutinesViewModel : RequestViewModel() {

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