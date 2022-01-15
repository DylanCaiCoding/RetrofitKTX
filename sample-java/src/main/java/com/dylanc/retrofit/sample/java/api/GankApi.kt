package com.dylanc.retrofit.sample.java.api

import com.dylanc.retrofit.interceptor.DomainName
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