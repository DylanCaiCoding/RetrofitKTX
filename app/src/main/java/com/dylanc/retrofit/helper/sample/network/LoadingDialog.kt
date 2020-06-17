package com.dylanc.retrofit.helper.sample.network

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager


class LoadingDialog : DialogFragment() {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      AlertDialog.Builder(it)
        .setTitle("loading")
        .setMessage("wait a minute...")
        .setCancelable(false)
        .create()
    } ?: throw IllegalStateException("Activity cannot be null")
  }

  fun show(fragmentManager: FragmentManager) {
    show(fragmentManager, "loading")
  }
}