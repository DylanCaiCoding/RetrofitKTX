package com.dylanc.retrofit.helper.sample

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dylanc.retrofit.helper.DownloadTask
import com.dylanc.retrofit.helper.RetrofitHelper
import com.dylanc.retrofit.helper.transformer.ThreadTransformer
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.body.ProgressInfo

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
const val DOWNLOAD_URL =
  "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563016090751&di=dd39865395e1bf58988a64fa3b077fe2&imgtype=0&src=http%3A%2F%2Fi1.hdslb.com%2Fbfs%2Farchive%2Fa724c9dedb0cb263c64a1f69571e92c9f2999a87.jpg"

class KotlinActivity : AppCompatActivity(), ProgressListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun requestBaiduNews(view: View) {
    RetrofitHelper.create(TestService::class)
      .baiduNews
      .compose(ThreadTransformer.main())
      .subscribe(object : Observer<String> {
        override fun onSubscribe(d: Disposable) {

        }

        override fun onNext(s: String) {
          Toast.makeText(this@KotlinActivity, s, Toast.LENGTH_SHORT).show()
        }

        override fun onError(e: Throwable) {
          Toast.makeText(this@KotlinActivity, e.message, Toast.LENGTH_SHORT).show()
        }

        override fun onComplete() {

        }
      })
  }

  fun requestGankData(view: View) {
    RetrofitHelper.create(TestService::class)
      .gankData
      .compose(ThreadTransformer.main())
      .subscribe(object : Observer<String> {
        override fun onComplete() {

        }

        override fun onSubscribe(d: Disposable) {

        }

        override fun onNext(s: String) {
          Toast.makeText(this@KotlinActivity, s, Toast.LENGTH_SHORT).show()
        }

        override fun onError(e: Throwable) {
          Toast.makeText(this@KotlinActivity, e.message, Toast.LENGTH_SHORT).show()
        }
      })
  }

  fun requestLogin(view: View) {
    RetrofitHelper.create(TestService::class)
      .login()
      .compose(ThreadTransformer.main())
      .subscribe(object : Observer<String> {
        override fun onSubscribe(d: Disposable) {

        }

        override fun onNext(s: String) {
          Toast.makeText(this@KotlinActivity, s, Toast.LENGTH_SHORT).show()
        }

        override fun onError(e: Throwable) {
          Toast.makeText(this@KotlinActivity, e.message, Toast.LENGTH_SHORT).show()
        }

        override fun onComplete() {

        }
      })
  }

  @SuppressLint("CheckResult")
  fun download(view: View) {
    val pathname = Environment.getExternalStorageDirectory().path.toString() + "/test.png"
    RxPermissions(this)
      .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
      .flatMap { granted ->
        if (!granted) {
          throw Exception("请授权访问文件权限")
        }
        DownloadTask.with(DOWNLOAD_URL)
          .progressListener(this@KotlinActivity)
          .downloadTo(pathname)
      }
      .subscribe(
        {
          Toast.makeText(this@KotlinActivity, "下载成功", Toast.LENGTH_SHORT).show()
        }, { e ->
          Toast.makeText(this@KotlinActivity, e.message, Toast.LENGTH_SHORT).show()
        })
  }

  override fun onProgress(progressInfo: ProgressInfo?) {
    Log.d("download", progressInfo?.percent.toString())
  }

  override fun onError(id: Long, e: java.lang.Exception?) {

  }
}