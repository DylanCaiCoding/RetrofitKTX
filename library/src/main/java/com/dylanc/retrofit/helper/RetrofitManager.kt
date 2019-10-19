@file:Suppress("unused")

package com.dylanc.retrofit.helper

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
object RetrofitManager {

  @JvmStatic
  fun putDomain(domainName: String, domainUrl: String) {
    RetrofitUrlManager.getInstance().putDomain(domainName, domainUrl)
  }

  @JvmStatic
  fun setGlobalDomain(globalDomain: String) {
    RetrofitUrlManager.getInstance().setGlobalDomain(globalDomain)
  }

  @JvmStatic
  fun clearCookieJar() {
    val cookieJar = RetrofitHelper.default.cookieJar
    if (cookieJar is PersistentCookieJar) {
      cookieJar.clear()
    }
  }
}
