package com.dylanc.retrofit.helper.sample.network

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.dylanc.retrofit.helper.rxjava.RequestLoading

class RxLoadingDialog(private val activity: FragmentActivity) : RequestLoading {

  private val loadingDialog = LoadingDialogFragment()

  override fun show() {
    loadingDialog.show(activity.supportFragmentManager, "loading")
  }

  override fun dismiss() {
    loadingDialog.dismiss()
  }

  class LoadingDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      return activity?.let {
        AlertDialog.Builder(it)
          .setTitle("loading")
          .setMessage("wait a minute...")
          .setCancelable(false)
          .create()
      } ?: throw IllegalStateException("Activity cannot be null")
    }
  }
}
