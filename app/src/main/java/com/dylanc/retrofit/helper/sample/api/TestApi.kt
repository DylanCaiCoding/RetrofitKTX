package com.dylanc.retrofit.helper.sample.api

import com.dylanc.retrofit.helper.DOMAIN_HEADER
import com.dylanc.retrofit.helper.sample.bean.ResultBean
import com.dylanc.retrofit.helper.sample.bean.UserBean
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Dylan Cai
 * @since 2019/4/17
 */
interface TestApi {

  @GET("/article/list/{page}/json")
  fun geArticleList(@Path(value = "page") page:Int): Single<String>

  @Headers(DOMAIN_HEADER + "gank")
  @GET("/api/today")
  fun getGankTodayList(): Single<String>

  @GET("/user/login")
  fun login(): Single<ResultBean<UserBean>>

  @Streaming
  @GET
  fun download(@Url url: String): Single<ResponseBody>
}