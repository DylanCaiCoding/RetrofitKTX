package com.dylanc.retrofit.helper.sample.network

import com.dylanc.retrofit.helper.apiOf
import com.dylanc.retrofit.helper.coroutines.request
import com.dylanc.retrofit.helper.coroutines.result
import com.dylanc.retrofit.helper.sample.api.CoroutinesApi
import com.dylanc.retrofit.helper.sample.api.GankApi
import com.dylanc.retrofit.helper.body.toFile

object DataRepository {

  suspend fun geArticleList() = request(responseHandler) {
    apiOf<CoroutinesApi>().geArticleList(0)
  }

  suspend fun getGankTodayList() = request(responseHandler) {
    apiOf<GankApi>().getGankTodayList()
  }

  suspend fun login() = result(responseHandler) {
    apiOf<CoroutinesApi>().login()
  }

  suspend fun download(url: String, path: String) = request(responseHandler) {
    apiOf<CoroutinesApi>().download(url).toFile(path)
  }
}