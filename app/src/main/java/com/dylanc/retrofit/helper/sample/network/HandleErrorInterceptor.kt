package com.dylanc.retrofit.helper.sample.network

import com.dylanc.retrofit.helper.interceptor.ResponseBodyInterceptor
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * @author Dylan Cai
 * @since 2019/10/11
 */
class HandleErrorInterceptor : ResponseBodyInterceptor() {

  override fun intercept(responseBody: ResponseBody, body: String) {
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
  }
}