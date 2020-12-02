package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.ViewModel


abstract class RequestViewModel : ViewModel() {
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()

  override fun onCleared() {
    super.onCleared()
    RequestProvider(this).clear()
  }
}