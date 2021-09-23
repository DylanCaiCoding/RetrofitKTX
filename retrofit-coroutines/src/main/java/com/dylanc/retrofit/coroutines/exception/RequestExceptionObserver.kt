package com.dylanc.retrofit.coroutines.exception

import android.widget.Toast
import androidx.fragment.app.FragmentActivity

/**
 * @author Dylan Cai
 */

internal var defaultExceptionObserver = RequestExceptionObserver { activity, e ->
  Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
}

fun interface RequestExceptionObserver {
  fun onChanged(activity: FragmentActivity, e: Throwable)
}
