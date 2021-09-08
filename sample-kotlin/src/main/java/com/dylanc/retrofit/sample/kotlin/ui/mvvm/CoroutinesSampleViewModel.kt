package com.dylanc.retrofit.sample.kotlin.ui.mvvm

import androidx.lifecycle.asLiveData
import com.dylanc.retrofit.coroutines.RequestViewModel
import com.dylanc.retrofit.coroutines.exception.catch
import com.dylanc.retrofit.coroutines.loading.showLoading
import com.dylanc.retrofit.sample.kotlin.repository.DataRepository

class CoroutinesSampleViewModel : RequestViewModel() {

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
