package com.dylanc.retrofit.helper.sample.ui

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.retrofit.helper.apiServices
import com.dylanc.retrofit.helper.autodispose.autoDispose
import com.dylanc.retrofit.helper.rxjava.RxDownloadApi
import com.dylanc.retrofit.helper.rxjava.download
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import com.dylanc.retrofit.helper.rxjava.toFile
import com.dylanc.retrofit.helper.sample.R
import com.dylanc.retrofit.helper.sample.data.api.GankApi
import com.dylanc.retrofit.helper.sample.data.api.RxJavaApi
import com.dylanc.retrofit.helper.sample.data.constant.DOWNLOAD_URL
import com.dylanc.retrofit.helper.sample.network.observeDownload

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
@Suppress("UNUSED_PARAMETER")
class KotlinRxJavaActivity : AppCompatActivity(R.layout.activity_common) {

  private val rxJavaApi : RxJavaApi by apiServices()
  private val gankApi :GankApi by apiServices()
  private val rxDownloadApi : RxDownloadApi by apiServices()

  /**
   * 测试普通请求
   */
  fun requestArticleList(view: View) {
    rxJavaApi.geArticleList(0)
      .io2mainThread()
//      .showLoading(LoadingDialog())
      .autoDispose(this)
      .subscribe({
        alert(it)
      }, {})
  }

  /**
   * 测试不同 base url 的请求
   */
  fun requestGankTodayList(view: View) {
    gankApi.getGankTodayListByRxJava()
      .io2mainThread()
//      .showLoading(LoadingDialog(this))
      .autoDispose(this)
      .subscribe({
        alert(it)
      }, { e ->
        toast(e.message)
      })
  }

  /**
   * 测试返回本地 json 的模拟请求
   */
  fun requestLogin(view: View) {
    rxJavaApi.login()
      .io2mainThread()
//      .showLoading(LoadingDialog(this))
      .autoDispose(this)
      .subscribe({ result ->
        toast("登录${result.data.userName}成功")
      }, { e ->
        toast(e.message)
      })
  }

  /**
   * 测试下载文件
   */
  fun download(view: View) {
    val pathname = externalCacheDir!!.path + "/test.png"
    rxDownloadApi.download(DOWNLOAD_URL, 100)
      .toFile(pathname)
      .observeDownload(DOWNLOAD_URL, { progressInfo ->
        Log.d("download", progressInfo.percent.toString())
      }, { _, _ ->
        Log.e("download", "下载失败")
      })
//      .showLoading(LoadingDialog(this))
      .autoDispose(this)
      .subscribe({ file ->
        toast("已下载到${file.path}")
      }, { e ->
        toast(e.message)
      })
  }

  private fun alert(msg: String) {
    AlertDialog.Builder(this)
      .setTitle("Response data")
      .setMessage(msg)
      .create()
      .show()
  }

  private fun toast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
  }
}
