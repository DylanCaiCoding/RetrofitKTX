package com.dylanc.retrofit.helper

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
object RetrofitManager {

  @JvmStatic
  fun setGlobalDomain(globalDomain: String) {
    RetrofitUrlManager.getInstance().setGlobalDomain(globalDomain)
  }

  @JvmStatic
  fun putDomain(domainName: String, domainUrl: String) {
    RetrofitUrlManager.getInstance().putDomain(domainName, domainUrl)
  }

  @JvmStatic
  fun setProgressRefreshTime(refreshTime: Int) {
    ProgressManager.getInstance().setRefreshTime(refreshTime)
  }

  @JvmStatic
  fun addDownloadListener(url: String, listener: ProgressListener) {
    ProgressManager.getInstance().addResponseListener(url, listener)
  }

  @JvmStatic
  fun addDiffDownloadListenerOnSameUrl(url: String, listener: ProgressListener) {
    ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, listener)
  }

  @JvmStatic
  fun addDiffDownloadListenerOnSameUrl(url: String, key: String, listener: ProgressListener) {
    ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, key, listener)
  }

  @JvmStatic
  fun addUploadListener(url: String, listener: ProgressListener) {
    ProgressManager.getInstance().addRequestListener(url, listener)
  }

  @JvmStatic
  fun addDiffUploadListenerOnSameUrl(url: String, listener: ProgressListener) {
    ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, listener)
  }

  @JvmStatic
  fun clearCookieJar(){
    val cookieJar = RetrofitHelper.default.cookieJar
    if (cookieJar is PersistentCookieJar) {
      cookieJar.clear()
    }
  }
}
