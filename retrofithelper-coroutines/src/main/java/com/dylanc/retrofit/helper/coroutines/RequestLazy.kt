package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.MutableLiveData
import java.lang.reflect.Constructor
import java.util.*
import kotlin.reflect.KClass


inline fun <reified T : Request> RequestViewModel.requests(
  isLoading: LoadingLiveData = this.isLoading,
  exception: ExceptionLiveData = this.exception
) =
  RequestLazy(T::class, isLoading, exception)

class RequestLazy<T : Request>(
  private val requestClass: KClass<T>,
  private val isLoading: LoadingLiveData,
  private val exception: ExceptionLiveData
) : Lazy<T> {
  private var cached: T? = null

  override val value: T
    get() {
      val constructor = requestClass.java.findMatchingConstructor(
        MutableLiveData::class.java,
        MutableLiveData::class.java
      ) ?: throw RuntimeException()
      return constructor.newInstance(isLoading, exception)
    }

  @Suppress("UNCHECKED_CAST")
  private fun <T> Class<T>.findMatchingConstructor(vararg signature: Class<*>): Constructor<T>? {
    for (constructor in constructors) {
      val parameterTypes = constructor.parameterTypes
      if (Arrays.equals(signature, parameterTypes)) {
        return constructor as Constructor<T>
      }
    }
    return null
  }

  override fun isInitialized() = cached != null
}