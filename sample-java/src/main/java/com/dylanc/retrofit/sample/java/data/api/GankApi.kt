package com.dylanc.retrofit.sample.java.data.api

import com.dylanc.retrofit.interceptor.DomainName
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

/**
 * @author Dylan Cai
 */
interface GankApi {

  @DomainName("gank")
  @GET("today")
  fun getGankTodayListByRxJava(): Single<String>
}