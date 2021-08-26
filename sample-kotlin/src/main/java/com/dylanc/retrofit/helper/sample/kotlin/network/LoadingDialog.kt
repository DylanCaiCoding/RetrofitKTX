package com.dylanc.retrofit.helper.sample.kotlin.network

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager


class LoadingDialog : DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return AlertDialog.Builder(requireContext())
      .setTitle("loading")
      .setMessage("wait a minute...")
      .setCancelable(false)
      .create()
  }

  fun show(manager: FragmentManager) {
    super.show(manager, TAG_LOADING)
  }

  companion object {
    const val TAG_LOADING = "loading"
  }
}