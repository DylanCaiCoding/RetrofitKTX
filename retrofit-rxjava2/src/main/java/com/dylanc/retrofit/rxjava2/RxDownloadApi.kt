@file:Suppress("unused")

package com.dylanc.retrofit.rxjava2

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

fun RxDownloadApi.download(url: String, startByte: Int) =
  download(url, "bytes=$startByte-")

fun RxDownloadApi.download(url: String, vararg bytesRange: Pair<Int?, Int?>): Single<ResponseBody> {
  val stringBuilder = StringBuilder()
  for ((start, end) in bytesRange) {
    if (stringBuilder.isBlank()) {
      stringBuilder.append("bytes=${start ?: ""}-${end ?: ""}")
    } else {
      stringBuilder.append(",${start ?: ""}-${end ?: ""}")
    }
  }
  return download(url, stringBuilder.toString())
}

interface RxDownloadApi {

  @Streaming
  @GET
  fun download(@Url url: String): Single<ResponseBody>

  @Streaming
  @GET
  fun download(@Url url: String, @Header("Range") range: String): Single<ResponseBody>
}
