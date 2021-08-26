package com.dylanc.retrofit.helper.sample.kotlin.data.api

import com.dylanc.retrofit.helper.sample.kotlin.data.bean.ApiResponse
import com.dylanc.retrofit.helper.sample.kotlin.data.bean.UserBean
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Dylan Cai
 */
interface RxJavaApi {

  @GET("/article/list/{page}/json")
  fun geArticleList(@Path(value = "page") page: Int): Single<String>

  @GET("/user/login")
  fun login(): Single<ApiResponse<UserBean>>
}