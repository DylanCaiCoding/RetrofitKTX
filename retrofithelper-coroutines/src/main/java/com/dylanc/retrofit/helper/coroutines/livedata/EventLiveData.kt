package com.dylanc.retrofit.helper.coroutines.livedata

import androidx.annotation.MainThread
import androidx.collection.arraySetOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Dylan Cai
 */
class EventLiveData<T> : MutableLiveData<T>() {

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

  @MainThread
  override fun setValue(t: T?) {
    observers.forEach { it.pending.set(true) }
    super.setValue(t)
  }

  private class ObserverWrapper<T>(val observer: Observer<T>) : Observer<T> {

    var pending = AtomicBoolean(false)

    override fun onChanged(t: T) {
      if (pending.compareAndSet(true, false)) {
        observer.onChanged(t)
      }
    }
  }
}