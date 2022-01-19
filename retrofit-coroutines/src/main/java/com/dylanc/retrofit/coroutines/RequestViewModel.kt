@file:Suppress("PropertyName")

package com.dylanc.retrofit.coroutines

import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.coroutines.exception.RequestExceptionObserver
import com.dylanc.retrofit.coroutines.exception.defaultExceptionObserver
import com.dylanc.retrofit.coroutines.loading.RequestLoadingObserver
import com.dylanc.retrofit.coroutines.loading.defaultLoadingObserver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

fun initRequestViewModel(
  loadingObserver: RequestLoadingObserver,
  exceptionObserver: RequestExceptionObserver? = null
) {
  defaultLoadingObserver = loadingObserver
  exceptionObserver?.let { defaultExceptionObserver = it }
}

abstract class RequestViewModel : ViewModel() {
  protected val _loadingFlow = MutableSharedFlow<Boolean>()
  val loadingFlow: SharedFlow<Boolean> get() = _loadingFlow

  protected val _exceptionFlow = MutableSharedFlow<Throwable>()
  val exceptionFlow: SharedFlow<Throwable> get() = _exceptionFlow
}
