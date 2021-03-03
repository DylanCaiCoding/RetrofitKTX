@file:Suppress("unused")

package com.dylanc.retrofit.helper.coroutines

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart


private const val TAG_LOADING = "loading"

typealias LoadingLiveData = MutableLiveData<Boolean>

typealias ExceptionLiveData = MutableLiveData<Throwable>

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.showLoading(isLoading: LoadingLiveData) =
  onStart { isLoading.value = true }
    .onCompletion { isLoading.value = false }

fun <T> Flow<T>.catch(exception: ExceptionLiveData) =
  catch { exception.value = it }

fun LoadingLiveData.observe(
  activity: FragmentActivity,
  dialog: DialogFragment,
  tag : String = TAG_LOADING
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
  tag : String = TAG_LOADING
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
