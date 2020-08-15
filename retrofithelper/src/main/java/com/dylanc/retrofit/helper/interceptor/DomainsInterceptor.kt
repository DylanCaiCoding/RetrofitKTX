package com.dylanc.retrofit.helper.interceptor

import com.dylanc.retrofit.helper.DOMAIN_HEADER
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.HashMap

/**
 * @author Dylan Cai
 */

fun OkHttpClient.Builder.addDomain(headerKey: String = DOMAIN_HEADER, name: String, value: String): OkHttpClient.Builder =
  addDomains(headerKey, hashMapOf(name to value))

fun OkHttpClient.Builder.addDomains(headerKey: String = DOMAIN_HEADER, headers: Map<String, String>): OkHttpClient.Builder =
  addInterceptor(DomainsInterceptor(headerKey, headers.toMutableMap()))

class DomainsInterceptor(
  private val headerKey: String,
  val domains: MutableMap<String, String>
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val baseUrl = request.url
    val builder = request.newBuilder()
    val headers = request.headers(headerKey)
    return if (headers.isEmpty()) {
      chain.proceed(request)
    } else {
      builder.removeHeader(headerKey)
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