package com.dylanc.retrofit.helper.sample.api

import com.dylanc.retrofit.helper.annotations.ApiUrl
import com.dylanc.retrofit.helper.sample.constant.URL_GANK
import com.dylanc.retrofit.helper.sample.network.DOMAIN_HEADER
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * @author Dylan Cai
 */
@ApiUrl(URL_GANK)
interface GankApi {

  @GET("/api/today")
  suspend fun getGankTodayListByCoroutines(): String

  @GET("/api/today")
  fun getGankTodayListByRxJava(): Single<String>
}