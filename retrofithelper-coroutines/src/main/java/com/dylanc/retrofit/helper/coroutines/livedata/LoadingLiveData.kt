@file:Suppress("unused")

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

private const val TAG_LOADING = "loading"

typealias LoadingLiveData = EventLiveData<Boolean>

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.showLoading(isLoading: LoadingLiveData) =
  onStart { isLoading.value = true }
    .onCompletion { isLoading.value = false }

fun LoadingLiveData.observe(
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

fun LoadingLiveData.observe(
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

fun LoadingLiveData.observe(activity: FragmentActivity, dialog: Dialog?) {
  observe(activity) {
    if (it) {
      dialog?.show()
    } else {
      dialog?.dismiss()
    }
  }
}

fun LoadingLiveData.observe(fragment: Fragment, dialog: Dialog?) {
  observe(fragment.viewLifecycleOwner) {
    if (it) {
      dialog?.show()
    } else {
      dialog?.dismiss()
    }
  }
}
