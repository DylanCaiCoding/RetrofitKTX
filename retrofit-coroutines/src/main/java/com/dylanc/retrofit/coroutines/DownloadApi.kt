@file:Suppress("unused")

package com.dylanc.retrofit.coroutines

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

suspend fun DownloadApi.download(url: String, startByte: Int) =
  download(url, "bytes=$startByte-")

suspend fun DownloadApi.download(url: String, vararg bytesRange: Pair<Int?, Int?>): ResponseBody {
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

interface DownloadApi {

  @Streaming
  @GET
  suspend fun download(@Url url: String): ResponseBody

  @Streaming
  @GET
  suspend fun download(@Url url: String, @Header("Range") range: String): ResponseBody
}
