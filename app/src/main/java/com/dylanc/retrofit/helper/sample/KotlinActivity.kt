package com.dylanc.retrofit.helper.sample

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dylanc.retrofit.helper.DownloadService
import com.dylanc.retrofit.helper.RetrofitHelper
import com.dylanc.retrofit.helper.UploadUtils
import com.dylanc.retrofit.helper.transformer.DownloadTransformer
import com.dylanc.retrofit.helper.transformer.LoadingTransformer
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

@Suppress("UNUSED_PARAMETER")
@SuppressLint("CheckResult")
class KotlinActivity : AppCompatActivity(), ProgressListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  fun requestBaiduNews(view: View) {
    RetrofitHelper.create(TestService::class)
      .baiduNews
      .compose(ThreadTransformer.main())
      .compose(LoadingTransformer.apply(this))
      .subscribe(this::onNext, this::onError)
  }

  fun requestGankData(view: View) {
    RetrofitHelper.create(TestService::class)
      .gankData
      .compose(ThreadTransformer.main())
      .compose(LoadingTransformer.apply(this))
      .subscribe(this::onNext, this::onError)
  }

  fun requestLogin(view: View) {
    RetrofitHelper.create(TestService::class)
      .login()
      .compose(ThreadTransformer.main())
      .subscribe(this::onNext, this::onError)
  }

  private fun onNext(json: String) {
    Toast.makeText(this, json, Toast.LENGTH_SHORT).show()
  }

  private fun onError(e: Throwable) {
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  }

  @SuppressLint("CheckResult")
  fun download(view: View) {
    val pathname = Environment.getExternalStorageDirectory().path.toString() + "/test.png"
    RxPermissions(this)
      .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
      .doOnNext { granted ->
        if (!granted) {
          throw Exception("请授权访问文件权限")
        }
      }
      .flatMap {
        RetrofitHelper.create(DownloadService::class)
          .download(DOWNLOAD_URL)
          .compose(DownloadTransformer.addDownListener(DOWNLOAD_URL, this@KotlinActivity))
          .compose(DownloadTransformer.downloadTo(pathname))
      }
      .subscribe(
        {
          Toast.makeText(this@KotlinActivity, "下载成功", Toast.LENGTH_SHORT).show()
        },
        this::onError
      )
  }

  override fun onProgress(progressInfo: ProgressInfo?) {
    Log.d("download", progressInfo?.percent.toString())
  }

  override fun onError(id: Long, e: java.lang.Exception?) {
//    UploadUtils.createPart()
  }
}