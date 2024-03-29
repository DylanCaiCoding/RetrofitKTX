package com.dylanc.retrofit.sample.kotlin.ui.mvp

import com.dylanc.retrofit.apiServices
import com.dylanc.retrofit.rxjava3.RxDownloadApi
import com.dylanc.retrofit.rxjava3.toFile
import com.dylanc.retrofit.sample.kotlin.api.GankApi
import com.dylanc.retrofit.sample.kotlin.api.RxJavaApi
import com.dylanc.retrofit.sample.kotlin.constant.DOWNLOAD_URL

/**
 * @author Dylan Cai
 */
class RxJavaSampleModel : RxJavaSampleContract.IModel {

  private val rxJavaApi: RxJavaApi by apiServices()
  private val gankApi: GankApi by apiServices()
  private val rxDownloadApi: RxDownloadApi by apiServices()

  override fun requestArticleList() =
    rxJavaApi.geArticleList(0)

  override fun requestGankTodayList() =
    gankApi.getGankTodayListByRxJava()

  override fun requestLogin() =
    rxJavaApi.login()

  override fun requestDownload(url: String, pathname: String) =
    rxDownloadApi.download(DOWNLOAD_URL).toFile(pathname)
}
