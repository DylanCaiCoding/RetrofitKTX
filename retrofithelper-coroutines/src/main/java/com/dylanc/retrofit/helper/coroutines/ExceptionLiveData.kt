package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch


@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.catch(exceptionLiveData: ExceptionLiveData) =
  catch { e ->
    exceptionLiveData._exception.value = e
  }

class ExceptionLiveData : LiveData<Throwable>() {
  @Suppress("PropertyName")
  internal val _exception: MutableLiveData<Throwable> = MutableLiveData()
  override fun observe(owner: LifecycleOwner, observer: Observer<in Throwable>) {
    _exception.observe(owner, observer)
  }

  override fun observeForever(observer: Observer<in Throwable>) {
    _exception.observeForever(observer)
  }

  override fun hasActiveObservers(): Boolean {
    return _exception.hasActiveObservers()
  }

  override fun hasObservers(): Boolean {
    return _exception.hasObservers()
  }

  override fun removeObserver(observer: Observer<in Throwable>) {
    _exception.removeObserver(observer)
  }

  override fun removeObservers(owner: LifecycleOwner) {
    _exception.removeObservers(owner)
  }

  override fun getValue(): Throwable? {
    return _exception.value
  }
}