@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.coroutines.livedata

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

/**
 * @author Dylan Cai
 */

typealias LoadingLiveData = RequestLiveData<Boolean>

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <T> Flow<T>.showLoading(isLoading: LoadingLiveData) =
  onStart { isLoading.value = true }
    .onCompletion { isLoading.value = false }

inline fun LoadingLiveData.observe(
  activity: FragmentActivity,
  dialogFragment: DialogFragment
) =
  observe(activity, LoadingObserver(activity.supportFragmentManager, dialogFragment))

inline fun LoadingLiveData.observe(
  fragment: Fragment,
  dialogFragment: DialogFragment
) =
  observe(fragment.viewLifecycleOwner, LoadingObserver(fragment.parentFragmentManager, dialogFragment))

inline fun LoadingLiveData.observe(lifecycleOwner: LifecycleOwner, dialog: Dialog?) {
  observe(lifecycleOwner) { isLoading ->
    if (isLoading && dialog?.isShowing != true) {
      dialog?.show()
    } else if (!isLoading && dialog?.isShowing == true) {
      dialog.dismiss()
    }
  }
}

@Suppress("FunctionName")
fun LoadingObserver(fragmentManager: FragmentManager, dialogFragment: DialogFragment) =
  Observer<Boolean> { isLoading ->
    if (isLoading && !dialogFragment.isShowing) {
      dialogFragment.show(fragmentManager, dialogFragment.toString())
    } else if (!isLoading && dialogFragment.isShowing) {
      dialogFragment.dismiss()
    }
  }

inline val DialogFragment.isShowing: Boolean
  get() = dialog?.isShowing == true && !isRemoving
