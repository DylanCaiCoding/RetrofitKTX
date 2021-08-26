package com.dylanc.retrofit.helper.sample.java.data.api

import com.dylanc.retrofit.helper.interceptor.DomainName
import io.reactivex.Single
import retrofit2.http.GET

/**
 * @author Dylan Cai
 */
interface GankApi {

  @DomainName("gank")
  @GET("today")
  fun getGankTodayListByRxJava(): Single<String>
}