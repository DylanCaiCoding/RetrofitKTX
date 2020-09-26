package com.dylanc.retrofit.helper.sample.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.rxjava.autoDispose
import com.dylanc.retrofit.helper.rxjava.io2mainThread
import com.dylanc.retrofit.helper.rxjava.showLoading
import com.dylanc.retrofit.helper.rxjava.toFile
import com.dylanc.retrofit.helper.sample.R
import com.dylanc.retrofit.helper.sample.api.GankApi
import com.dylanc.retrofit.helper.sample.api.RxJavaApi
import com.dylanc.retrofit.helper.sample.constant.DOWNLOAD_URL
import com.dylanc.retrofit.helper.sample.network.observeDownload
import com.dylanc.retrofit.helper.sample.network.rx.RxLoadingDialog
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
@Suppress("UNUSED_PARAMETER")
class KotlinRxJavaActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_common)
  }

  /**
   * 测试普通请求
   */
  fun requestArticleList(view: View) {
    apiServiceOf<RxJavaApi>()
      .geArticleList(0)
      .io2mainThread()
      .showLoading(RxLoadingDialog(this))
      .autoDispose(this)
      .subscribe({
        alert(it)
      }, {})
  }

  /**
   * 测试不同 base url 的请求
   */
  fun requestGankTodayList(view: View) {
    apiServiceOf<GankApi>()
      .getGankTodayListByRxJava()
      .io2mainThread()
      .showLoading(RxLoadingDialog(this))
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
    apiServiceOf<RxJavaApi>()
      .login()
      .io2mainThread()
      .showLoading(RxLoadingDialog(this))
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
    RxPermissions(this)
      .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
      .autoDispose(this)
      .subscribe { granted ->
        if (!granted) {
          toast("请授权访问文件权限")
        } else {
          apiServiceOf<RxJavaApi>()
            .download(DOWNLOAD_URL)
            .toFile(pathname)
            .observeDownload(DOWNLOAD_URL, { progressInfo ->
              Log.d("download", progressInfo.percent.toString())
            }, { _, _ ->
              Log.e("download", "下载失败")
            })
            .showLoading(RxLoadingDialog(this))
            .autoDispose(this)
            .subscribe({ file ->
              toast("已下载到${file.path}")
            }, { e ->
              toast(e.message)
            })
        }
      }
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
