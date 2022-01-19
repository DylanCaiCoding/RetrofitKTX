@file:Suppress("unused")

package com.dylanc.retrofit.coroutines

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun LiveData<Boolean>.observe(activity: FragmentActivity, dialogFragment: DialogFragment) =
  observe(activity) { dialogFragment.show(activity.supportFragmentManager, it) }

fun LiveData<Boolean>.observe(fragment: Fragment, dialogFragment: DialogFragment) =
  observe(fragment.viewLifecycleOwner) { dialogFragment.show(fragment.parentFragmentManager, it) }

fun LiveData<Boolean>.observe(lifecycleOwner: LifecycleOwner, dialog: Dialog?) {
  observe(lifecycleOwner) { dialog.show(it) }
}

fun Dialog?.show(isLoading: Boolean) {
  if (isLoading && this?.isShowing != true) {
    this?.show()
  } else if (!isLoading && this?.isShowing == true) {
    this.dismiss()
  }
}

fun DialogFragment.show(fragmentManager: FragmentManager, isShow: Boolean) {
  if (isShow && !isShowing) {
    show(fragmentManager, toString())
  } else if (!isShow && isShowing) {
    dismiss()
  }
}

inline val DialogFragment.isShowing: Boolean
  get() = dialog?.isShowing == true && !isRemoving
