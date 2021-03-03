@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NOTHING_TO_INLINE")
@file:JvmName("Domain")

package com.dylanc.retrofit.helper.interceptor

import com.dylanc.retrofit.helper.RetrofitHelper
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.*

/**
 * @author Dylan Cai
 */

private const val DOMAIN = "Domain-Name"
const val DOMAIN_HEADER = "$DOMAIN:"

inline fun OkHttpClient.Builder.putDomains(domains: MutableMap<String, String>) =
  addInterceptor(DomainsInterceptor(domains))

inline fun RetrofitHelper.Builder.putDomains(domains: MutableMap<String, String>) =
  addInterceptor(DomainsInterceptor(domains))

class DomainsInterceptor(
  val domains: MutableMap<String, String>
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val baseUrl = request.url
    val builder = request.newBuilder()
    val headers = request.headers(DOMAIN)
    return if (headers.isEmpty()) {
      chain.proceed(request)
    } else {
      builder.removeHeader(DOMAIN)
      val headerValue = headers[0]
      val url = if (domains.containsKey(headerValue)) {
        domains[headerValue]?.toHttpUrlOrNull()!!
      } else {
        baseUrl
      }
      val newFullUrl = request.url.newBuilder()
        .apply {
          domains[headerValue]?.let {
            for (i in 0 until baseUrl.pathSize) {
              removePathSegment(0)
            }
            url.encodedPathSegments + baseUrl.encodedPathSegments
              .forEach {
                addEncodedPathSegment(it)
              }
          }
        }
        .scheme(url.scheme)
        .host(url.host)
        .port(url.port)
        .build()
      chain.proceed(builder.url(newFullUrl).build())
    }
  }
}