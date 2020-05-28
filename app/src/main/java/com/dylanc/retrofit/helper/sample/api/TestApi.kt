package com.dylanc.retrofit.helper.sample.api

import com.dylanc.retrofit.helper.DOMAIN_HEADER
import com.dylanc.retrofit.helper.sample.bean.ResultBean
import com.dylanc.retrofit.helper.sample.bean.UserBean
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * @author Dylan Cai
 * @since 2019/4/17
 */
interface TestApi {

  @GET("/guonei")
  fun getBaiduNews(): Single<String>

  @Headers(DOMAIN_HEADER + "gank")
  @GET("/api/today")
  fun getGankData(): Single<String>

  @GET("/user/login")
  fun login(): Single<ResultBean<UserBean>>
}