package com.dylanc.retrofit.helper.sample.repository

import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.sample.api.CoroutinesApi
import com.dylanc.retrofit.helper.sample.api.GankApi
import com.dylanc.retrofit.helper.body.toFile
import kotlinx.coroutines.flow.flow

object DataRepository {

  fun geArticleList() = flow {
    emit(apiServiceOf<CoroutinesApi>().geArticleList(0))
  }

  fun getGankTodayList() = flow {
    emit(apiServiceOf<GankApi>().getGankTodayListByCoroutines())
  }

  fun login() = flow {
    emit(apiServiceOf<CoroutinesApi>().login())
  }

  fun download(url: String, path: String) = flow {
    emit(apiServiceOf<CoroutinesApi>().download(url).toFile(path))
  }
}