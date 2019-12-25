package com.dylanc.retrofit.helper.sample.network

import com.dylanc.retrofit.helper.interceptor.ResponseBodyInterceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

/**
 * @author Dylan Cai
 * @since 2019/10/11
 */
class HandleErrorInterceptor : ResponseBodyInterceptor() {

  override fun intercept(response: Response, url: String, body: String): Response {
    var jsonObject: JSONObject? = null
    try {
      jsonObject = JSONObject(body)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    if (jsonObject != null) {
      if (jsonObject.optInt("code", -1) != 200 && jsonObject.has("msg")) {
        throw RuntimeException(jsonObject.getString("msg"))
      }
    }
    return response
  }
}