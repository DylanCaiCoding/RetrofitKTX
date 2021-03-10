@file:Suppress("unused", "MemberVisibilityCanBePrivate", "NOTHING_TO_INLINE")
@file:JvmName("Domain")

package com.dylanc.retrofit.helper.interceptor

import com.dylanc.retrofit.helper.RetrofitHelper
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Invocation

/**
 * @author Dylan Cai
 */

inline fun OkHttpClient.Builder.putDomains(domains: Map<String, String>) =
  addInterceptor(DomainsInterceptor(domains))

inline fun RetrofitHelper.Builder.putDomains(domains: Map<String, String>) =
  addInterceptor(DomainsInterceptor(domains))

class DomainsInterceptor(
  private val domains: Map<String, String>
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val baseUrl = request.url
    val builder = request.newBuilder()
    val invocation = request.tag(Invocation::class.java)
    val domainName = invocation?.method()?.getAnnotation(DomainName::class.java)?.value
    return if (invocation == null && domainName.isNullOrBlank()) {
      chain.proceed(request)
    } else {
      val url = domains[domainName]?.toHttpUrlOrNull() ?: baseUrl
      val newFullUrl = request.url.newBuilder()
        .apply {
          domains[domainName]?.let {
            for (i in 0 until baseUrl.pathSize) {
              removePathSegment(0)
            }
            (url.encodedPathSegments + baseUrl.encodedPathSegments).forEach {
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