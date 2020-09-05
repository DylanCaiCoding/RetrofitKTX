@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.sample.network

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

/**
 * @author Dylan Cai
 */

private const val DOMAIN = "Domain"
const val DOMAIN_HEADER = "$DOMAIN:"

inline fun OkHttpClient.Builder.putDomains(vararg pairs: Pair<String, String>) =
  putDomains(hashMapOf(*pairs))

inline fun OkHttpClient.Builder.putDomains(domains: MutableMap<String, String>) = apply {
  if (domains.isNotEmpty()) addInterceptor(DomainsInterceptor(domains))
}

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
        .scheme(url.scheme)
        .host(url.host)
        .port(url.port)
        .build()
      chain.proceed(builder.url(newFullUrl).build())
    }
  }
}