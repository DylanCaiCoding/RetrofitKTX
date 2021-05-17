package com.dylanc.retrofit.helper.sample.ui

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.retrofit.helper.coroutines.observeException
import com.dylanc.retrofit.helper.coroutines.observeLoading
import com.dylanc.retrofit.helper.sample.R
import com.dylanc.retrofit.helper.sample.data.constant.DOWNLOAD_URL
import com.dylanc.retrofit.helper.sample.network.LoadingDialog

@Suppress("UNUSED_PARAMETER")
class KotlinCoroutinesActivity : AppCompatActivity(R.layout.activity_common) {

  private val viewModel: KotlinCoroutinesViewModel by viewModels()
  private val loadingDialog by lazy { LoadingDialog(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.allRequest
      .observeLoading(this) {
        loadingDialog.show(it)
      }
    viewModel.allRequest
      .observeException(this) {
        toast(it.message)
      }
  }

  /**
   * 测试普通请求
   */
  fun requestArticleList(view: View) {
    viewModel.testRequest
      .geArticleList()
      .observe(this, {
        alert(it)
      })
  }

  /**
   * 测试不同 base url 的请求
   */
  fun requestGankTodayList(view: View) {
    viewModel.testRequest.getGankTodayList()
      .observe(this, {
        alert(it)
      })
  }

  /**
   * 测试返回本地 json 的模拟请求
   */
  fun requestLogin(view: View) {
    viewModel.loginRequest.login()
      .observe(this, { response ->
        toast("登录${response!!.data.userName}成功")
      })
  }

  /**
   * 测试下载文件
   */
  fun download(view: View) {
    val pathname = externalCacheDir!!.absolutePath + "/test.png"
    viewModel.downloadRequest.download(DOWNLOAD_URL, pathname)
      .observe(this) {
        toast("已下载 ${it.path}")
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