@file:Suppress("PropertyName")

package com.dylanc.retrofit.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dylanc.retrofit.coroutines.exception.RequestExceptionObserver
import com.dylanc.retrofit.coroutines.exception.defaultExceptionObserver
import com.dylanc.retrofit.coroutines.loading.RequestLoadingObserver
import com.dylanc.retrofit.coroutines.loading.defaultLoadingObserver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * @author Dylan Cai
 */

fun initRequestViewModel(
  loadingObserver: RequestLoadingObserver,
  exceptionObserver: RequestExceptionObserver? = null
) {
  defaultLoadingObserver = loadingObserver
  exceptionObserver?.let { defaultExceptionObserver = it }
}

abstract class RequestViewModel : ViewModel() {
  protected val loadingFlow = MutableSharedFlow<Boolean>()
  val isLoading: SharedFlow<Boolean> get() = loadingFlow

  protected val exceptionFlow = MutableSharedFlow<Throwable>()
  val exception: SharedFlow<Throwable> get() = exceptionFlow
}
