package com.dylanc.retrofit.coroutines.loading

import androidx.fragment.app.FragmentActivity

/**
 * @author Dylan Cai
 */


internal var defaultLoadingObserver: RequestLoadingObserver? = null

abstract class RequestLoadingObserver {
  abstract fun onCreate(activity: FragmentActivity)

  abstract fun onChanged(activity: FragmentActivity, isLoading: Boolean)

  abstract fun onDestroy()
}
