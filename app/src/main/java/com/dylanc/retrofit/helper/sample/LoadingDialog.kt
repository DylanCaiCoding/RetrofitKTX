package com.dylanc.retrofit.helper.sample

import android.app.Dialog
import android.content.Context
import android.support.v7.app.AlertDialog
import com.dylanc.retrofit.helper.ILoadingDialog

/**
 * @author Dylan Cai
 * @since 2019/8/15
 */
class LoadingDialog : ILoadingDialog {
  override fun onCreate(context: Context): Dialog = AlertDialog.Builder(context)
    .setTitle("loading")
    .setMessage("wait for a minute...")
    .show()
}