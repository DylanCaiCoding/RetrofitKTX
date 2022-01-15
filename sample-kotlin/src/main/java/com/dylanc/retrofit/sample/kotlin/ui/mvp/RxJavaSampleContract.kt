package com.dylanc.retrofit.sample.kotlin.ui.mvp

import android.content.Context
import com.dylanc.retrofit.rxjava3.RequestLoading
import com.dylanc.retrofit.sample.kotlin.bean.ApiResponse
import com.dylanc.retrofit.sample.kotlin.bean.UserBean
import io.reactivex.rxjava3.core.Single
import java.io.File

/**
 * @author Dylan Cai
 */
class RxJavaSampleContract {

  interface IView {
    fun setResultText(msg: String)
    fun toast(msg: String?)
    val context: Context
    val loadingDialog: RequestLoading
  }

  interface IPresenter {
    fun onArticleListBtnClick()
    fun onTodayListBtnClick()
    fun onLoginBtnClick()
    fun onDownloadBtnClick()
  }

  interface IModel {
    fun requestArticleList(): Single<String>
    fun requestGankTodayList(): Single<String>
    fun requestLogin(): Single<ApiResponse<UserBean>>
    fun requestDownload(url: String, pathname: String): Single<File>
  }
}
