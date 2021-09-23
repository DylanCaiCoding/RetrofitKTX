@file:Suppress("unused")

package com.dylanc.retrofit.rxjava

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

/**
 * @author Dylan Cai
 */
fun interface RequestLoading {

  fun onRequestLoading(isLoading: Boolean)

  companion object {
    fun create(dialog: Dialog) = RequestLoading {
      if (it && !dialog.isShowing) {
        dialog.show()
      } else if (!it && dialog.isShowing) {
        dialog.dismiss()
      }
    }

    fun create(fragmentManager: FragmentManager, dialogFragment: DialogFragment) = RequestLoading {
      if (it && !dialogFragment.isShowing) {
        dialogFragment.show(fragmentManager, toString())
      } else if (!it && dialogFragment.isShowing) {
        dialogFragment.dismiss()
      }
    }

    private val DialogFragment.isShowing: Boolean
      get() = dialog?.isShowing == true && !isRemoving
  }
}