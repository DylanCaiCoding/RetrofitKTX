package com.dylanc.retrofit.coroutines.livedata

import androidx.annotation.MainThread
import androidx.collection.arraySetOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * @author Dylan Cai
 */
class RequestLiveData<T> : MutableLiveData<T>() {

  private val observers = arraySetOf<ObserverWrapper<in T>>()

  @MainThread
  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    super.observe(owner, ObserverWrapper(observer).also { observers.add(it) })
  }

  @MainThread
  override fun observeForever(observer: Observer<in T>) {
    super.observeForever(ObserverWrapper(observer).also { observers.add(it) })
  }

  @MainThread
  override fun removeObserver(observer: Observer<in T>) {
    observers.removeAll {
      if (it.observer == observer) super.removeObserver(observer)
      it.observer == observer
    }
  }

  override fun onInactive() {
    observers.forEach { it.pending = false }
  }

  @MainThread
  override fun setValue(t: T?) {
    observers.forEach { it.pending = true }
    super.setValue(t)
  }

  private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

    var pending = false

    override fun onChanged(t: T) {
      if (pending) {
        observer.onChanged(t)
        pending = false
      }
    }
  }
}
