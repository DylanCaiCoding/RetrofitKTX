package com.dylanc.retrofit.sample.kotlin.ui.mvp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dylanc.retrofit.coroutines.livedata.show
import com.dylanc.retrofit.rxjava.RequestLoading
import com.dylanc.retrofit.sample.kotlin.databinding.ActivitySampleBinding
import com.dylanc.retrofit.sample.kotlin.widget.LoadingDialogFragment

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
@Suppress("UNUSED_PARAMETER")
class RxJavaSampleActivity : AppCompatActivity(), RxJavaSampleContract.IView {

  private val loadingDialogFragment by lazy { LoadingDialogFragment() }
  private var _presenter: RxJavaSamplePresenter? = null
  private val presenter get() = _presenter!!
  override val context: Context get() = this

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(ActivitySampleBinding.inflate(layoutInflater).root)
    _presenter = RxJavaSamplePresenter(this, this)
  }

  /**
   * 测试普通请求
   */
  fun onArticleListBtnClick(view: View) {
    presenter.onArticleListBtnClick()
  }

  /**
   * 测试不同 base url 的请求
   */
  fun onTodayListBtnClick(view: View) {
    presenter.onTodayListBtnClick()
  }

  /**
   * 测试返回本地 json 的模拟请求
   */
  fun onLoginBtnClick(view: View) {
    presenter.onLoginBtnClick()
  }

  /**
   * 测试下载文件
   */
  fun onDownloadBtnClick(view: View) {
    presenter.onDownloadBtnClick()
  }

  override fun alert(msg: String) {
    AlertDialog.Builder(this)
      .setTitle("Response data")
      .setMessage(msg)
      .create()
      .show()
  }

  override fun toast(msg: String?) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
  }

  override val loadingDialog: RequestLoading
    get() = RequestLoading { isLoading ->
      loadingDialogFragment.show(supportFragmentManager, isLoading)
    }

  override fun onDestroy() {
    super.onDestroy()
    _presenter = null
  }
}
