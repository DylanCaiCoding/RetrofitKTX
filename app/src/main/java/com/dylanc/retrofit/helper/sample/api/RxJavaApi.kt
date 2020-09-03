package com.dylanc.retrofit.helper.sample.api

import com.dylanc.retrofit.helper.interceptor.DOMAIN_HEADER
import com.dylanc.retrofit.helper.sample.bean.ApiResponse
import com.dylanc.retrofit.helper.sample.bean.UserBean
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Dylan Cai
 * @since 2019/4/17
 */
interface RxJavaApi {

  @GET("/article/list/{page}/json")
  fun geArticleList(@Path(value = "page") page:Int): Single<String>

  @Headers(DOMAIN_HEADER + "gank")
  @GET("/api/today")
  fun getGankTodayList(): Single<String>

  @GET("/user/login")
  fun login(): Single<ApiResponse<UserBean>>

  @Streaming
  @GET
  fun download(@Url url: String): Single<ResponseBody>
}