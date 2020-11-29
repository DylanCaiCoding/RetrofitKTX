package com.dylanc.retrofit.helper.sample.network

import okhttp3.Response
import org.json.JSONObject

/**
 * @author Dylan Cai
 * @since 2019/10/11
 */
object GlobalErrorHandler  {

  @Suppress("UNUSED_PARAMETER")
  @JvmStatic
  fun handleResponse(response: Response, url: String, body: String): Response {
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