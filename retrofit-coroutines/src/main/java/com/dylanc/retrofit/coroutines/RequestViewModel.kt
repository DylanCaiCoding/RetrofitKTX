package com.dylanc.retrofit.coroutines

import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.coroutines.exception.ExceptionLiveData
import com.dylanc.retrofit.coroutines.exception.RequestExceptionObserver
import com.dylanc.retrofit.coroutines.livedata.LoadingLiveData
import com.dylanc.retrofit.coroutines.livedata.show
import com.dylanc.retrofit.coroutines.loading.RequestDialogFragmentFactory
import com.dylanc.retrofit.coroutines.loading.RequestLoadingObserver

/**
 * @author Dylan Cai
 */

internal var defaultLoadingObserver: RequestLoadingObserver? = null
internal var defaultErrorObserver = RequestExceptionObserver { activity, e ->
  Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
}

fun initRequestViewModel(factory: RequestDialogFragmentFactory, exceptionObserver: RequestExceptionObserver? = null) {
  val requestLoadingObserver = object : RequestLoadingObserver() {
    private var dialogFragment: DialogFragment? = null
    override fun onCreate(activity: FragmentActivity) {
      if (dialogFragment == null) {
        dialogFragment = factory.create()
      }
    }

    override fun onChanged(activity: FragmentActivity, isLoading: Boolean) {
      dialogFragment?.show(activity.supportFragmentManager, isLoading)
    }

    override fun onDestroy() {
      dialogFragment = null
    }
  }
  initRequestViewModel(requestLoadingObserver, exceptionObserver)
}

fun initRequestViewModel(loadingObserver: RequestLoadingObserver, exceptionObserver: RequestExceptionObserver? = null) {
  defaultLoadingObserver = loadingObserver
  if (exceptionObserver != null) {
    defaultErrorObserver = exceptionObserver
  }
}

abstract class RequestViewModel : ViewModel() {
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()
}
