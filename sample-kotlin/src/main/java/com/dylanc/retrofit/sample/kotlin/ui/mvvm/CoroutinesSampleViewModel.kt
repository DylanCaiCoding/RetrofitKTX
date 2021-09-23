package com.dylanc.retrofit.sample.kotlin.ui.mvvm

import androidx.lifecycle.asLiveData
import com.dylanc.retrofit.coroutines.RequestViewModel
import com.dylanc.retrofit.coroutines.catchWith
import com.dylanc.retrofit.coroutines.showLoadingWith
import com.dylanc.retrofit.sample.kotlin.repository.DataRepository

class CoroutinesSampleViewModel : RequestViewModel() {

  fun geArticleList() =
    DataRepository.geArticleList()
      .showLoadingWith(loadingFlow)
      .catchWith(exceptionFlow)
      .asLiveData()

  fun getGankTodayList() =
    DataRepository.getGankTodayList()
      .showLoadingWith(loadingFlow)
      .catchWith(exceptionFlow)
      .asLiveData()

  fun login() =
    DataRepository.login()
      .showLoadingWith(loadingFlow)
      .catchWith(exceptionFlow)
      .asLiveData()

  fun download(url: String, path: String) =
    DataRepository.download(url, path)
      .showLoadingWith(loadingFlow)
      .catchWith(exceptionFlow)
      .asLiveData()
}
