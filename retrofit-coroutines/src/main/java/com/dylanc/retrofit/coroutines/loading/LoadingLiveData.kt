@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.coroutines.livedata

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.dylanc.retrofit.coroutines.RequestLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * @author Dylan Cai
 */

typealias LoadingLiveData = RequestLiveData<Boolean>

inline fun <T> Flow<T>.showLoading(isLoading: LoadingLiveData) =
  onStart { isLoading.value = true }
    .onCompletion { isLoading.value = false }

inline fun LoadingLiveData.observe(activity: FragmentActivity, dialogFragment: DialogFragment) =
  observe(activity) { dialogFragment.show(activity.supportFragmentManager, it) }

inline fun LoadingLiveData.observe(fragment: Fragment, dialogFragment: DialogFragment) =
  observe(fragment.viewLifecycleOwner) { dialogFragment.show(fragment.parentFragmentManager, it) }

inline fun LoadingLiveData.observe(lifecycleOwner: LifecycleOwner, dialog: Dialog?) {
  observe(lifecycleOwner) { dialog.show(it) }
}

fun Dialog?.show(isLoading: Boolean) {
  if (isLoading && this?.isShowing != true) {
    this?.show()
  } else if (!isLoading && this?.isShowing == true) {
    this.dismiss()
  }
}

inline fun DialogFragment.show(fragmentManager: FragmentManager, isShow: Boolean) {
  if (isShow && !isShowing) {
    show(fragmentManager, toString())
  } else if (!isShow && isShowing) {
    dismiss()
  }
}

inline val DialogFragment.isShowing: Boolean
  get() = dialog?.isShowing == true && !isRemoving
