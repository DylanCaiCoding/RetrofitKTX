package com.dylanc.retrofit.helper.sample.network

import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.coroutines.request
import com.dylanc.retrofit.helper.coroutines.result
import com.dylanc.retrofit.helper.sample.api.CoroutinesApi
import com.dylanc.retrofit.helper.sample.api.GankApi
import com.dylanc.retrofit.helper.toFile

object DataRepository {

  suspend fun geArticleList() = request(responseHandler) {
    apiServiceOf<CoroutinesApi>().geArticleList(0)
  }

  suspend fun getGankTodayList() = request(responseHandler) {
    apiServiceOf<GankApi>().getGankTodayList()
  }

  suspend fun login() = result(responseHandler) {
    apiServiceOf<CoroutinesApi>().login()
  }

  suspend fun download(url: String, path: String) = request(responseHandler) {
    apiServiceOf<CoroutinesApi>().download(url).toFile(path)
  }
}