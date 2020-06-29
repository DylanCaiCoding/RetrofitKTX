package com.dylanc.retrofit.helper.sample.network.rx

import androidx.fragment.app.FragmentActivity
import com.dylanc.retrofit.helper.rxjava.RequestLoading
import com.dylanc.retrofit.helper.sample.network.LoadingDialog

class RxLoadingDialog(private val activity: FragmentActivity) : RequestLoading {

  private val loadingDialog =
    LoadingDialog()

  override fun show(isShow: Boolean) {
    if (isShow){
      loadingDialog.show(activity.supportFragmentManager)
    }else{
      loadingDialog.dismiss()
    }
  }
}

