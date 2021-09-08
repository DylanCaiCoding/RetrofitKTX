package com.dylanc.retrofit.coroutines.loading

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

/**
 * @author Dylan Cai
 */
abstract class RequestDialogFragmentFactory : RequestLoadingObserver() {
  private var dialogFragment: DialogFragment? = null

  override fun onCreate(activity: FragmentActivity) {
    if (dialogFragment == null) {
      dialogFragment = create()
    }
  }

  override fun onChanged(activity: FragmentActivity, isLoading: Boolean) {
    dialogFragment?.show(activity.supportFragmentManager, isLoading)
  }

  override fun onDestroy() {
    dialogFragment = null
  }

  abstract fun create(): DialogFragment
}
