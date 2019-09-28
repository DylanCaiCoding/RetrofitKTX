package com.dylanc.retrofit.helper.sample.api

import com.dylanc.retrofit.helper.sample.bean.ResultBean
import com.dylanc.retrofit.helper.sample.bean.UserBean

import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author Dylan Cai
 * @since 2019/4/17
 */
interface TestService {

  @GET("guonei")
  fun getBaiduNews(): Observable<String>

  @Headers("Domain-Name: gank")
  @GET("/api/today")
  fun getGankData(): Observable<String>

  @GET("/user/login")
  fun login(): Observable<ResultBean<UserBean>>
}
