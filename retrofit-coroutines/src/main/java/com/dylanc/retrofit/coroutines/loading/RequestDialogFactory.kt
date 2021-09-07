package com.dylanc.retrofit.coroutines.loading

import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.dylanc.retrofit.coroutines.livedata.show

/**
 * @author Dylan Cai
 */
abstract class RequestDialogFactory : RequestLoadingObserver() {
  private var dialog: Dialog? = null

  override fun onCreate(activity: FragmentActivity) {
    if (dialog == null) {
      dialog = create(activity)
    }
  }

  override fun onChanged(activity: FragmentActivity, isLoading: Boolean) {
    dialog?.show(isLoading)
  }

  override fun onDestroy() {
    dialog = null
  }

  abstract fun create(context: Context): Dialog
}
