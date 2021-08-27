package com.dylanc.retrofit.sample.kotlin.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.retrofit.coroutines.requestViewModels
import com.dylanc.retrofit.sample.kotlin.R
import com.dylanc.retrofit.sample.kotlin.data.constant.DOWNLOAD_URL

@Suppress("UNUSED_PARAMETER")
class CoroutinesActivity : AppCompatActivity(R.layout.activity_sample) {

  private val viewModel: CoroutinesViewModel by requestViewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.user.observe(this) {
      Log.d("test", "onCreate: $it")
    }
  }

  /**
   * 测试普通请求
   */
  fun requestArticleList(view: View) {
    viewModel.geArticleList()
      .observe(this) {
        alert(it)
      }
  }

  /**
   * 测试不同 base url 的请求
   */
  fun requestGankTodayList(view: View) {
    viewModel.getGankTodayList()
      .observe(this) {
        alert(it)
      }
  }

  /**
   * 测试返回本地 json 的模拟请求
   */
  fun requestLogin(view: View) {
    viewModel.login()
      .observe(this) { response ->
        toast("登录${response!!.data.userName}成功")
      }
  }

  /**
   * 测试下载文件
   */
  fun download(view: View) {
    val pathname = externalCacheDir!!.absolutePath + "/test.png"
    viewModel.download(DOWNLOAD_URL, pathname)
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
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
  }
}