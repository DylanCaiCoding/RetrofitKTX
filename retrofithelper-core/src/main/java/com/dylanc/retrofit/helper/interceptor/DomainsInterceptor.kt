@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.interceptor

import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.Invocation

/**
 * @author Dylan Cai
 */

inline fun OkHttpClient.Builder.putDomains(domains: MutableMap<String, String>) =
  addInterceptor(DomainsInterceptor(domains))

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class DomainName(val value: String)

class DomainsInterceptor(
  private val domains: MutableMap<String, String>
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