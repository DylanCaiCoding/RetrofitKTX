@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.coroutines.livedata

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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
  dialog: DialogFragment,
  tag: String = TAG_LOADING
) {
  observe(activity) {
    if (it) {
      dialog.show(activity.supportFragmentManager, tag)
    } else if (dialog.dialog != null) {
      dialog.dismiss()
    }
  }
}

inline fun LoadingLiveData.observe(
  fragment: Fragment,
  dialog: DialogFragment,
  tag: String = TAG_LOADING
) {
  observe(fragment.viewLifecycleOwner) {
    if (it) {
      dialog.show(fragment.parentFragmentManager, tag)
    } else if (dialog.dialog != null) {
      dialog.dismiss()
    }
  }
}

inline fun LoadingLiveData.observe(activity: FragmentActivity, dialog: Dialog?) {
  observe(activity) { isLoading ->
    dialog?.let { if (isLoading) it.show() else it.dismiss() }
  }
}

inline fun LoadingLiveData.observe(fragment: Fragment, dialog: Dialog?) {
  observe(fragment.viewLifecycleOwner) { isLoading ->
    dialog?.let { if (isLoading) it.show() else it.dismiss() }
  }
}
