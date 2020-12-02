package com.dylanc.retrofit.helper.sample.data.api

import com.dylanc.retrofit.helper.interceptor.DOMAIN_HEADER
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * @author Dylan Cai
 */
interface GankApi {

  @Headers(DOMAIN_HEADER + "gank")
  @GET("today")
  suspend fun getGankTodayListByCoroutines(): String

  @Headers(DOMAIN_HEADER + "gank")
  @GET("today")
  fun getGankTodayListByRxJava(): Single<String>
}