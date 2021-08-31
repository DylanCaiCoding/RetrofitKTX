package com.dylanc.retrofit.coroutines

import androidx.fragment.app.FragmentActivity

/**
 * @author Dylan Cai
 */
fun interface RequestErrorObserver {
  fun onChanged(activity: FragmentActivity, e: Throwable)
}
