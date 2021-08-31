package com.dylanc.retrofit.sample.kotlin.ui.mvp

import androidx.lifecycle.LifecycleOwner
import com.dylanc.retrofit.autodispose.autoDispose
import com.dylanc.retrofit.rxjava.io2mainThread
import com.dylanc.retrofit.rxjava.showLoading
import com.dylanc.retrofit.sample.kotlin.data.constant.DOWNLOAD_URL

/**
 * @author Dylan Cai
 */
class RxJavaSamplePresenter(
  private val lifecycleOwner: LifecycleOwner,
  private val view: RxJavaSampleContract.IView
) : RxJavaSampleContract.IPresenter {

  private val model = RxJavaSampleModel()

  override fun onFirstBtnClick() {
    model.requestArticleList()
      .io2mainThread()
      .showLoading(view.loadingDialog)
      .autoDispose(lifecycleOwner)
      .subscribe({
        view.alert(it)
      }, {
        view.toast(it.message)
      })
  }

  override fun onSecondBtnClick() {
    model.requestGankTodayList()
      .io2mainThread()
      .showLoading(view.loadingDialog)
      .autoDispose(lifecycleOwner)
      .subscribe({
        view.alert(it)
      }, { e ->
        view.toast(e.message)
      })
  }

  override fun onThirdBtnClick() {
    model.requestLogin()
      .io2mainThread()
      .showLoading(view.loadingDialog)
      .autoDispose(lifecycleOwner)
      .subscribe({ result ->
        view.toast("登录${result.data.userName}成功")
      }, { e ->
        view.toast(e.message)
      })
  }

  override fun onFourthBtnClick() {
    val pathname = view.context.externalCacheDir!!.path + "/test.png"
    model.requestDownload(DOWNLOAD_URL, pathname)
//      .observeDownload(DOWNLOAD_URL, { progressInfo ->
//        Log.d("download", progressInfo.percent.toString())
//      }, { _, _ ->
//        Log.e("download", "下载失败")
//      })
      .showLoading(view.loadingDialog)
      .autoDispose(lifecycleOwner)
      .subscribe({ file ->
        view.toast("已下载到${file.path}")
      }, { e ->
        view.toast(e.message)
      })
  }
}