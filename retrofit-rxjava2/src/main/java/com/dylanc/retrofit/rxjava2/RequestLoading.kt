@file:Suppress("unused")

package com.dylanc.retrofit.rxjava2

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * @author Dylan Cai
 */
fun interface RequestLoading {

  fun onRequestLoading(isLoading: Boolean)

  companion object {
    const val TAG_LOADING = "request_loading"

    fun create(dialog: Dialog) = RequestLoading {
      if (it && !dialog.isShowing) {
        dialog.show()
      } else if (!it && dialog.isShowing) {
        dialog.dismiss()
      }
    }

    fun create(fragmentManager: FragmentManager, dialogFragment: DialogFragment, tag: String = TAG_LOADING) =
      RequestLoading {
        if (it && !dialogFragment.isShowing) {
          dialogFragment.show(fragmentManager, tag)
        } else if (!it && dialogFragment.isShowing) {
          dialogFragment.dismiss()
        }
      }

    private val DialogFragment.isShowing: Boolean
      get() = dialog?.isShowing == true && !isRemoving
  }
}
