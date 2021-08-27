@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.coroutines.livedata

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * @author Dylan Cai
 */

const val TAG_LOADING = "loading"

typealias LoadingLiveData = RequestLiveData<Boolean>

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <T> Flow<T>.showLoading(isLoading: LoadingLiveData) =
  onStart { isLoading.value = true }
    .onCompletion { isLoading.value = false }

inline fun LoadingLiveData.observe(
  activity: FragmentActivity,
  dialogFragment: DialogFragment,
  tag: String = TAG_LOADING
) {
  observe(activity) { isLoading ->
    if (isLoading && !dialogFragment.isShowing) {
      dialogFragment.show(activity.supportFragmentManager, tag)
    } else if (!isLoading && dialogFragment.isShowing) {
      dialogFragment.dismiss()
    }
  }
}

inline fun LoadingLiveData.observe(
  fragment: Fragment,
  dialogFragment: DialogFragment,
  tag: String = TAG_LOADING
) {
  observe(fragment.viewLifecycleOwner) { isLoading ->
    if (isLoading && !dialogFragment.isShowing) {
      dialogFragment.show(fragment.parentFragmentManager, tag)
    } else if (!isLoading && dialogFragment.isShowing) {
      dialogFragment.dismiss()
    }
  }
}

inline fun LoadingLiveData.observe(lifecycleOwner: LifecycleOwner, dialog: Dialog?) {
  observe(lifecycleOwner) { isLoading ->
    if (isLoading && dialog?.isShowing != true) {
      dialog?.show()
    } else if (!isLoading && dialog?.isShowing == true) {
      dialog.dismiss()
    }
  }
}

inline val DialogFragment.isShowing: Boolean
  get() = dialog?.isShowing == true && !isRemoving