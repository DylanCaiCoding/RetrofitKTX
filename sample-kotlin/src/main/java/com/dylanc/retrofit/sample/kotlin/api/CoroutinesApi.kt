package com.dylanc.retrofit.sample.kotlin.api

import com.dylanc.retrofit.sample.kotlin.bean.ApiResponse
import com.dylanc.retrofit.sample.kotlin.bean.UserBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * @author Dylan Cai
 */
interface CoroutinesApi {

  @Headers("Accept: application/json")
  @GET("/article/list/{page}/json")
  suspend fun geArticleList(@Path(value = "page") page: Int): String

  @GET("/user/login")
  suspend fun login(): ApiResponse<UserBean>
}
