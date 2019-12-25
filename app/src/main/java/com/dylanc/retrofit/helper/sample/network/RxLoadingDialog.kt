package com.dylanc.retrofit.helper.sample.network

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import com.dylanc.retrofit.helper.RequestLoading
import com.dylanc.retrofit.helper.transformer.showLoading
import io.reactivex.Observable

/**
 * @author Dylan Cai
 * @since 2019/8/15
 */
fun <T> Observable<T>.showLoading(context: Context): Observable<T> =
  showLoading(RxLoadingDialog(context))

class RxLoadingDialog(private val context: Context) : RequestLoading {
  private val dialog: Dialog by lazy {
    AlertDialog.Builder(context)
      .setTitle("loading")
      .setMessage("wait a minute...")
      .create()
  }

  override fun show() {
    dialog.show()
  }

  override fun dismiss() {
    dialog.dismiss()
  }
}