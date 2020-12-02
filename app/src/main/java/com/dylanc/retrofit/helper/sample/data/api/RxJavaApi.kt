package com.dylanc.retrofit.helper.sample.data.api

import com.dylanc.retrofit.helper.sample.data.bean.ApiResponse
import com.dylanc.retrofit.helper.sample.data.bean.UserBean
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author Dylan Cai
 */
interface RxJavaApi {

  @GET("/article/list/{page}/json")
  fun geArticleList(@Path(value = "page") page:Int): Single<String>

  @GET("/user/login")
  fun login(): Single<ApiResponse<UserBean>>

  @Streaming
  @GET
  fun download(@Url url: String): Single<ResponseBody>
}