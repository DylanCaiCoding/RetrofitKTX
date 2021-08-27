package com.dylanc.retrofit.sample.java.network

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class LoadingDialog : DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return AlertDialog.Builder(requireContext())
      .setTitle("loading")
      .setMessage("wait a minute...")
      .setCancelable(false)
      .create()
  }
}