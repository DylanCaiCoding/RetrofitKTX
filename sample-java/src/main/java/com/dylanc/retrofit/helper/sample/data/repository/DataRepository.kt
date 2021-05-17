package com.dylanc.retrofit.helper.sample.data.repository

import android.content.Context
import com.dylanc.retrofit.helper.apiServiceOf
import com.dylanc.retrofit.helper.body.displayName
import com.dylanc.retrofit.helper.body.toFile
import com.dylanc.retrofit.helper.body.toMediaImageUri
import com.dylanc.retrofit.helper.coroutines.DownloadApi
import com.dylanc.retrofit.helper.sample.data.api.CoroutinesApi
import com.dylanc.retrofit.helper.sample.data.api.GankApi
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
    emit(apiServiceOf<DownloadApi>().download(url).toFile(path))
  }
}