package com.dylanc.retrofit.sample.kotlin.widget

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

/**
 * @author Dylan Cai
 */
class LoadingDialogFragment : DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return AlertDialog.Builder(requireContext())
      .setTitle("loading")
      .setMessage("wait a minute...")
      .setCancelable(false)
      .create()
  }
}
