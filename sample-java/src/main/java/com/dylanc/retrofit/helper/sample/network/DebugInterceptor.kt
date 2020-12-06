package com.dylanc.retrofit.helper.sample.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

/**
 * @author Dylan Cai
 */
class DebugInterceptor(
  private val context: Context,
  private val debugUrl: String,
  private val debugRawId: Int
) : Interceptor {

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val url = chain.request().url.toString()
    return if (url.contains(debugUrl)) {
      createDebugResponse(chain, debugRawId)
    } else {
      chain.proceed(chain.request())
    }
  }

  private fun createDebugResponse(chain: Interceptor.Chain, rawId: Int): Response {
    val inputStream = context.resources.openRawResource(rawId)
    val json = inputStream.bufferedReader().use { it.readText() }
    return Response.Builder()
      .code(200)
      .addHeader("Content-Type", "application/json")
      .addHeader("Content-Length", json.length.toString())
      .body(json.toResponseBody("application/json".toMediaTypeOrNull()))
      .message("OK")
      .request(chain.request())
      .protocol(Protocol.HTTP_1_1)
      .build()
  }
}