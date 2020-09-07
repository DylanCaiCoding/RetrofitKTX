package com.dylanc.retrofit.helper.sample.network

import com.dylanc.retrofit.helper.apiOf
import com.dylanc.retrofit.helper.sample.api.CoroutinesApi
import com.dylanc.retrofit.helper.sample.api.GankApi
import com.dylanc.retrofit.helper.body.toFile
import kotlinx.coroutines.flow.flow

object DataRepository {

  fun geArticleList() = flow {
    emit(apiOf<CoroutinesApi>().geArticleList(0))
  }

  fun getGankTodayList() = flow {
    emit(apiOf<GankApi>().getGankTodayListByCoroutines())
  }

  fun login() = flow {
    emit(apiOf<CoroutinesApi>().login())
  }

  fun download(url: String, path: String) = flow {
    emit(apiOf<CoroutinesApi>().download(url).toFile(path))
  }
}