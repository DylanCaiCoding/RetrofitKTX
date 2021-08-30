package com.dylanc.retrofit.coroutines

import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.coroutines.livedata.ExceptionLiveData
import com.dylanc.retrofit.coroutines.livedata.LoadingLiveData
import com.dylanc.retrofit.coroutines.livedata.show

/**
 * @author Dylan Cai
 */

internal var defaultLoadingObserver: RequestLoadingObserver? = null
  private set
internal var defaultErrorObserver: RequestErrorObserver = { activity, e ->
  Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
}
typealias RequestDialogFactory = () -> DialogFragment
typealias RequestErrorObserver = (activity: FragmentActivity, e: Throwable) -> Unit

fun initRequestViewModel(factory: RequestDialogFactory, errorObserver: RequestErrorObserver? = null) {
  val requestLoadingObserver = object : RequestLoadingObserver() {
    private var dialogFragment: DialogFragment? = null
    override fun onCreate(activity: FragmentActivity) {
      dialogFragment = factory()
    }

    override fun onChanged(activity: FragmentActivity, isLoading: Boolean) {
      dialogFragment?.show(activity.supportFragmentManager, isLoading)
    }

    override fun onDestroy() {
      dialogFragment = null
    }
  }
  initRequestViewModel(requestLoadingObserver, errorObserver)
}

fun initRequestViewModel(loadingObserver: RequestLoadingObserver, errorObserver: RequestErrorObserver? = null) {
  defaultLoadingObserver = loadingObserver
  if (errorObserver != null) {
    defaultErrorObserver = errorObserver
  }
}

abstract class RequestViewModel : ViewModel() {
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()
}