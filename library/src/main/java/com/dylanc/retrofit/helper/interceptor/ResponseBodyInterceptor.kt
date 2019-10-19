package com.dylanc.retrofit.helper.interceptor

import okhttp3.Interceptor
import okhttp3.ResponseBody
import okio.Buffer
import okio.GzipSource
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

/**
 * @author Dylan Cai
 * @since 2019/10/11
 */
abstract class ResponseBodyInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain) = chain.proceed(chain.request()).apply {
    body?.let { responseBody ->
      val contentLength = responseBody.contentLength()
      val source = responseBody.source()
      source.request(Long.MAX_VALUE)
      var buffer = source.buffer

      if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
        GzipSource(buffer.clone()).use { gzippedResponseBody ->
          buffer = Buffer()
          buffer.writeAll(gzippedResponseBody)
        }
      }

      val contentType = responseBody.contentType()
      val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
      if (contentLength != 0L) {
        intercept(responseBody,buffer.clone().readString(charset))
      }
    }
  }

  abstract fun intercept(responseBody: ResponseBody,body: String)
}