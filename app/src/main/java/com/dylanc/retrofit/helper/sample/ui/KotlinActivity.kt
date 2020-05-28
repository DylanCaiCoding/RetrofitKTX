package com.dylanc.retrofit.helper.sample.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.DownloadApi
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import com.dylanc.retrofit.helper.rxjava.toFile
import com.dylanc.retrofit.helper.sample.R
import com.dylanc.retrofit.helper.sample.api.TestApi
import com.dylanc.retrofit.helper.sample.network.observeDownload
import com.dylanc.retrofit.helper.sample.network.showLoadingDialog
import com.rxjava.rxlife.life
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
const val DOWNLOAD_URL =
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563016090751&di=dd39865395e1bf58988a64fa3b077fe2&imgtype=0&src=http%3A%2F%2Fi1.hdslb.com%2Fbfs%2Farchive%2Fa724c9dedb0cb263c64a1f69571e92c9f2999a87.jpg"

@Suppress("UNUSED_PARAMETER")
@SuppressLint("CheckResult")
class KotlinActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  /**
   * 测试普通请求
   */
  fun requestBaiduNews(view: View) {
    apiServiceOf<TestApi>()
      .getBaiduNews()
      .io2mainThread()
      .showLoadingDialog(this)
      .life(this)
      .subscribe({
        showToast(it)
      }, {
        showToast(it.message)
      })
  }

  /**
   * 测试不同 base url 的请求
   */
  fun requestGankData(view: View) {
    apiServiceOf<TestApi>()
      .getGankData()
      .io2mainThread()
      .showLoadingDialog(this)
      .life(this)
      .subscribe({
        showToast(it)
      }, {
        showToast(it.message)
      })
  }

  /**
   * 测试返回本地 json 的模拟请求
   */
  fun requestLogin(view: View) {
    apiServiceOf<TestApi>()
      .login()
      .io2mainThread()
      .showLoadingDialog(this)
      .life(this)
      .subscribe({ result ->
        showToast("登录${result.data.userName}成功")
      }, {
        showToast(it.message)
      })
  }

  /**
   * 测试下载文件
   */
  fun download(view: View) {
    val pathname = externalCacheDir!!.path + "/test.png"
    RxPermissions(this)
      .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
      .subscribe { granted ->
        if (!granted) {
          showToast("请授权访问文件权限")
        } else {
          apiServiceOf<DownloadApi>()
            .download(DOWNLOAD_URL)
            .toFile(pathname)
            .observeDownload(DOWNLOAD_URL, { progressInfo ->
              Log.d("download", progressInfo.percent.toString())
            }, { _, _ ->
              Log.e("download", "下载失败")
            })
            .showLoadingDialog(this)
            .life(this)
            .subscribe({
              showToast("下载成功")
            }, {
              showToast(it.message)
            })
        }
      }
  }

  private fun showToast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
  }
}
