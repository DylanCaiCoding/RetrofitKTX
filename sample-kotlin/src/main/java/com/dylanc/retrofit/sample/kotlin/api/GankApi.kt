package com.dylanc.retrofit.sample.kotlin.api

import com.dylanc.retrofit.ApiUrl
import com.dylanc.retrofit.interceptor.DomainName
import com.dylanc.retrofit.sample.kotlin.constant.URL_GANK
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

/**
 * @author Dylan Cai
 */
//@ApiUrl(URL_GANK)
interface GankApi {

  @DomainName("gank")
  @GET("today")
  suspend fun getGankTodayListByCoroutines(): String

  @DomainName("gank")
  @GET("today")
  fun getGankTodayListByRxJava(): Single<String>
}
