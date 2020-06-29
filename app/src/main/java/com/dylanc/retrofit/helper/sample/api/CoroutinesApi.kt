package com.dylanc.retrofit.helper.sample.api

import com.dylanc.retrofit.helper.DOMAIN_HEADER
import com.dylanc.retrofit.helper.sample.bean.ApiResponse
import com.dylanc.retrofit.helper.sample.bean.UserBean
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Dylan Cai
 */
interface CoroutinesApi {

  @GET("/article/list/{page}/json")
  suspend fun geArticleList(@Path(value = "page") page: Int): String

  @Headers(DOMAIN_HEADER + "gank")
  @GET("/api/today")
  suspend fun getGankTodayList(): String

  @GET("/user/login")
  suspend fun login(): ApiResponse<UserBean>

  @Streaming
  @GET
  suspend fun download(@Url url: String): ResponseBody
}