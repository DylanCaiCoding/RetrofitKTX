package com.dylanc.retrofit.helper.rxjava

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * @author Dylan Cai
 */
interface DownloadApi {

  @Streaming
  @GET
  fun download(@Url url: String): Observable<ResponseBody>
}