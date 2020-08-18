package com.dylanc.retrofit.helper.sample.api

import com.dylanc.retrofit.helper.annotations.ApiUrl
import com.dylanc.retrofit.helper.sample.constant.URL_GANK
import retrofit2.http.GET

@ApiUrl(URL_GANK)
interface GankApi {

  @GET("/api/today")
  suspend fun getGankTodayList(): String
}