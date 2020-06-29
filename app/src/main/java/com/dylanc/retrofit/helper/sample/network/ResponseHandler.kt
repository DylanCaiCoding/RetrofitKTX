package com.dylanc.retrofit.helper.sample.network

import com.dylanc.retrofit.helper.sample.bean.ApiResponse

val responseHandler: (Any) -> Unit = { response ->
  if (response is ApiResponse<*>) {
    val (code, msg) = response
    if (code == -1) {
      throw ApiException(msg)
    }
  }
}