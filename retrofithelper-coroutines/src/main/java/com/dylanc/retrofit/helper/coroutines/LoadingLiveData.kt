@file:Suppress("unused")

package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart


@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.showLoading(loadingLiveData: LoadingLiveData) =
  onStart { loadingLiveData._loading.value = true }
    .onCompletion { loadingLiveData._loading.value = false }

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.showLoading(loadingLiveData: MutableLiveData<Boolean>) =
  onStart { loadingLiveData.value = true }
    .onCompletion { loadingLiveData.value = false }

class LoadingLiveData : LiveData<Boolean>() {
  @Suppress("PropertyName")
  internal val _loading: MutableLiveData<Boolean> = MutableLiveData()
  override fun observe(owner: LifecycleOwner, observer: Observer<in Boolean>) {
    _loading.observe(owner, observer)
  }

  override fun observeForever(observer: Observer<in Boolean>) {
    _loading.observeForever(observer)
  }

  override fun hasActiveObservers(): Boolean {
    return _loading.hasActiveObservers()
  }

  override fun hasObservers(): Boolean {
    return _loading.hasObservers()
  }

  override fun removeObserver(observer: Observer<in Boolean>) {
    _loading.removeObserver(observer)
  }

  override fun removeObservers(owner: LifecycleOwner) {
    _loading.removeObservers(owner)
  }

  override fun getValue(): Boolean? {
    return _loading.value
  }
}