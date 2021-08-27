package com.dylanc.retrofit.coroutines

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.coroutines.livedata.ExceptionLiveData
import com.dylanc.retrofit.coroutines.livedata.LoadingLiveData

/**
 * @author Dylan Cai
 */

internal var defaultLoadingDialogFactory: (() -> DialogFragment)? = null
  private set
internal var defaultExceptionObserver: Observer<Throwable>? = null
  private set

fun initRequestViewModel(loadingDialogFactory: () -> DialogFragment, exceptionObserver: Observer<Throwable>) {
  defaultLoadingDialogFactory = loadingDialogFactory
  defaultExceptionObserver = exceptionObserver
}

abstract class RequestViewModel : ViewModel() {
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()
}
