package com.dylanc.retrofit.helper.sample

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dylanc.retrofit.helper.DownloadService
import com.dylanc.retrofit.helper.RetrofitHelper
import com.dylanc.retrofit.helper.create
import com.dylanc.retrofit.helper.sample.api.TestService
import com.dylanc.retrofit.helper.transformer.*
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.body.ProgressInfo
import java.io.File

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
    RetrofitHelper.create<TestService>()
      .getBaiduNews()
      .io2mainThread()
      .showLoading(this)
      .subscribe(this::onNext, this::onError)
  }

  /**
   * 测试不同 base url 的请求
   */
  fun requestGankData(view: View) {
    RetrofitHelper.create<TestService>()
      .getGankData()
      .io2mainThread()
      .showLoading(this)
      .subscribe(this::onNext, this::onError)
  }

  /**
   * 测试返回本地 json 的模拟请求
   */
  fun requestLogin(view: View) {
    RetrofitHelper.create<TestService>()
      .login()
      .io2mainThread()
      .showLoading(this)
      .subscribe({ result ->
        showToast("登录${result.data.userName}成功")
      }, this::onError)
  }

  /**
   * 测试下载文件
   */
  fun download(view: View) {
    val pathname = externalCacheDir.path + "/test.png"
    RxPermissions(this)
      .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
      .doOnNext { granted ->
        if (!granted) {
          throw Exception("请授权访问文件权限")
        }
      }
      .flatMap {
        RetrofitHelper.create<DownloadService>()
          .download(DOWNLOAD_URL)
          .addDownloadListener(DOWNLOAD_URL, downloadListener)
          .toFile(pathname)
          .showLoading(this)
      }
      .subscribe(
        {
          showToast("下载成功")
        },
        this::onError
      )
  }

  private val downloadListener = object : ProgressListener {
    override fun onProgress(progressInfo: ProgressInfo?) {
      Log.d("download", progressInfo?.percent.toString())
    }

    override fun onError(id: Long, e: java.lang.Exception?) {
      showToast("下载失败")
    }
  }

  private fun onNext(json: String) {
    showToast(json)
  }

  private fun onError(e: Throwable) {
    showToast(e.message)
  }

  private fun showToast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
  }
}