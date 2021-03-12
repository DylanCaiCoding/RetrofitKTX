@file:Suppress("unused")

package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.LifecycleOwner
import com.dylanc.retrofit.helper.coroutines.livedata.ExceptionLiveData
import com.dylanc.retrofit.helper.coroutines.livedata.LoadingLiveData

inline fun <reified T : ViewModelRequest> requests() = lazy {
  RequestProvider.get(T::class.java)
}

fun List<ViewModelRequest>.observeLoading(owner: LifecycleOwner, observer: (Boolean) -> Unit) =
  forEach {
    it.isLoading.observe(owner, observer)
  }

fun List<ViewModelRequest>.observeException(owner: LifecycleOwner, observer: (Throwable) -> Unit) =
  forEach {
    it.exception.observe(owner, observer)
  }

abstract class ViewModelRequest {
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()
}