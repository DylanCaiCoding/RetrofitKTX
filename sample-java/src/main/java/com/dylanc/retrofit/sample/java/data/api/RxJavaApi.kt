package com.dylanc.retrofit.sample.java.data.api

import com.dylanc.retrofit.sample.java.data.bean.ApiResponse
import com.dylanc.retrofit.sample.java.data.bean.UserBean
import io.reactivex.rxjava3.core.Single
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