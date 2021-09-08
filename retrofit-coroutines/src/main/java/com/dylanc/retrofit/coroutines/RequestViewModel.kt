package com.dylanc.retrofit.coroutines

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.coroutines.exception.ExceptionLiveData
import com.dylanc.retrofit.coroutines.exception.RequestExceptionObserver
import com.dylanc.retrofit.coroutines.loading.LoadingLiveData
import com.dylanc.retrofit.coroutines.loading.RequestLoadingObserver

/**
 * @author Dylan Cai
 */

internal var defaultLoadingObserver: RequestLoadingObserver? = null
internal var defaultErrorObserver = RequestExceptionObserver { activity, e ->
  Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
}

fun initRequestViewModel(
  loadingObserver: RequestLoadingObserver,
  exceptionObserver: RequestExceptionObserver? = null
) {
  defaultLoadingObserver = loadingObserver
  exceptionObserver?.let { defaultErrorObserver = it }
}

abstract class RequestViewModel : ViewModel() {
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()
}
