package com.dylanc.retrofit.helper.sample.network

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AlertDialog
import com.dylanc.retrofit.helper.rxjava.RequestLoading
import com.dylanc.retrofit.helper.rxjava.showLoading
import io.reactivex.Observable

private const val TAG_LOADING = "loading"

fun <T> Observable<T>.showLoading(activity: FragmentActivity): Observable<T> =
  showLoading(RxLoadingDialog(activity.supportFragmentManager))

class RxLoadingDialog(private val manager: FragmentManager) : RequestLoading {

  private val loadingDialog = LoadingDialog()

  override fun show() {
    loadingDialog.show(manager, TAG_LOADING)
  }

  override fun dismiss() {
    loadingDialog.dismiss()
  }
}

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
}