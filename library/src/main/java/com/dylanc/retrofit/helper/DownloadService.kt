package com.dylanc.retrofit.helper

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
interface DownloadService {

  @Streaming
  @GET
  fun download(@Url url: String):Observable<ResponseBody>
}