package com.dylanc.retrofit.sample.kotlin.ui.mvp

import android.content.Context
import com.dylanc.retrofit.rxjava.RequestLoading
import com.dylanc.retrofit.sample.kotlin.data.bean.ApiResponse
import com.dylanc.retrofit.sample.kotlin.data.bean.UserBean
import io.reactivex.Single
import java.io.File

/**
 * @author Dylan Cai
 */
class RxJavaSampleContract {

  interface IView {
    fun alert(msg: String)
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