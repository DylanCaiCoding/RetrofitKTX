package com.dylanc.retrofit.helper.sample.api

import com.dylanc.retrofit.helper.sample.network.DOMAIN_HEADER
import com.dylanc.retrofit.helper.sample.bean.ApiResponse
import com.dylanc.retrofit.helper.sample.bean.UserBean
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Dylan Cai
 */
interface CoroutinesApi {

  @GET("/article/list/{page}/json")
  suspend fun geArticleList(@Path(value = "page") page: Int): String

  @GET("/user/login")
  suspend fun login(@Body requestBody: RequestBody): ApiResponse<UserBean>

  @Streaming
  @GET
  suspend fun download(@Url url: String): ResponseBody
}