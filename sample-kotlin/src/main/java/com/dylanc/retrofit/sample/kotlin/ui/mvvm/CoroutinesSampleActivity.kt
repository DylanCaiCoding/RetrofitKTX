package com.dylanc.retrofit.sample.kotlin.ui.mvvm

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.retrofit.coroutines.requestViewModels
import com.dylanc.retrofit.sample.kotlin.constant.DOWNLOAD_URL
import com.dylanc.retrofit.sample.kotlin.databinding.ActivitySampleBinding

@Suppress("UNUSED_PARAMETER")
class CoroutinesSampleActivity : AppCompatActivity() {

  private val viewModel: CoroutinesSampleViewModel by requestViewModels()
  private lateinit var binding: ActivitySampleBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySampleBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }

  /**
   * 测试普通请求
   */
  fun onArticleListBtnClick(view: View) {
    viewModel.geArticleList()
      .observe(this) {
        binding.tvRequestResult.text = it
      }
  }

  /**
   * 测试不同 base url 的请求
   */
  fun onTodayListBtnClick(view: View) {
    viewModel.getGankTodayList()
      .observe(this) {
        binding.tvRequestResult.text = it
      }
  }

  /**
   * 测试返回本地 json 的模拟请求
   */
  fun onLoginBtnClick(view: View) {
    viewModel.login()
      .observe(this) { response ->
        toast("登录${response!!.data.userName}成功")
      }
  }

  /**
   * 测试下载文件
   */
  fun onDownloadBtnClick(view: View) {
    val pathname = externalCacheDir!!.absolutePath + "/test.png"
    viewModel.download(DOWNLOAD_URL, pathname)
      .observe(this) {
        toast("已下载 ${it.path}")
      }
  }

  private fun toast(msg: String?) {
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
  }
}
