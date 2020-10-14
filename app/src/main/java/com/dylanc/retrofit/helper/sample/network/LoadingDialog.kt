package com.dylanc.retrofit.helper.sample.network

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.dylanc.retrofit.helper.rxjava.RequestLoading


class LoadingDialog(
  private val fragmentActivity: FragmentActivity
) : DialogFragment(), RequestLoading {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return AlertDialog.Builder(fragmentActivity)
        .setTitle("loading")
        .setMessage("wait a minute...")
        .setCancelable(false)
        .create()
  }

  override fun show(isLoading: Boolean) {
    if (isLoading) {
      show(fragmentActivity.supportFragmentManager, "loading")
    } else {
      dismiss()
    }
  }
}