package com.dylanc.retrofit.sample.kotlin.network

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.dylanc.retrofit.coroutines.loading.RequestDialogFactory

/**
 * @author Dylan Cai
 */
class LoadingDialogFactory : RequestDialogFactory() {

  override fun create(context: Context): Dialog {
    return AlertDialog.Builder(context)
      .setTitle("loading")
      .setMessage("wait a minute...")
      .setCancelable(false)
      .create()
  }
}
