package com.dylanc.retrofit.helper.sample.network

import okhttp3.Response
import com.dylanc.retrofit.helper.interceptor.ResponseBodyInterceptor
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

/**
 * @author Dylan Cai
 * @since 2019/10/22
 */
class HandleLoginInterceptor : ResponseBodyInterceptor() {

  override fun intercept(response: Response, url: String, body: String): Response {
    var jsonObject: JSONObject? = null
    try {
      jsonObject = JSONObject(body)
      if (url.contains("login")) {
        if (jsonObject.getJSONObject("msg") != null) {
          jsonObject.put("data", jsonObject.getJSONObject("msg"))
          jsonObject.put("msg", "登录成功")
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

    val contentType = response.body?.contentType()
    val responseBody = jsonObject.toString().toResponseBody(contentType)
    return response.newBuilder().body(responseBody).build()
  }
}