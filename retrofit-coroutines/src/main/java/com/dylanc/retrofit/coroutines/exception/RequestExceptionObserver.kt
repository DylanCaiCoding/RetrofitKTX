package com.dylanc.retrofit.coroutines.exception

import androidx.fragment.app.FragmentActivity

/**
 * @author Dylan Cai
 */
fun interface RequestExceptionObserver {
  fun onChanged(activity: FragmentActivity, e: Throwable)
}
