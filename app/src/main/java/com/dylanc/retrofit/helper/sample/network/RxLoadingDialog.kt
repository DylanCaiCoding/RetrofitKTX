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
import io.reactivex.Single

fun <T> Single<T>.showLoadingDialog(activity: FragmentActivity): Single<T> =
  showLoading(RxLoadingDialog(activity))

class RxLoadingDialog(private val activity: FragmentActivity) : RequestLoading {

  private val loadingDialog = LoadingDialog()

  override fun show() {
    loadingDialog.show(activity.supportFragmentManager, TAG_LOADING)
  }

  override fun dismiss() {
    loadingDialog.dismiss()
  }

  companion object {
    private const val TAG_LOADING = "loading"
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
}
