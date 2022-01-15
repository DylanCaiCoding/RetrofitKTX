@file:Suppress("unused")

package com.dylanc.retrofit.interceptor

import com.dylanc.retrofit.getMethodAnnotation
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

val retrofitDomains = mutableMapOf<String, String>()

fun OkHttpClient.Builder.multipleDomains(vararg pairs: Pair<String, String>) =
  addInterceptor(DomainsInterceptor(retrofitDomains.apply { putAll(pairs) }))

fun OkHttpClient.Builder.multipleDomains(domains: MutableMap<String, String> = retrofitDomains) =
  addInterceptor(DomainsInterceptor(domains))

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class DomainName(val value: String)

class DomainsInterceptor(private val domains: MutableMap<String, String>) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val domainName = request.getMethodAnnotation<DomainName>()?.value
    return if (!domainName.isNullOrBlank()) {
      val baseUrl = request.url
      val builder = request.newBuilder()
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
    } else {
      chain.proceed(request)
    }
  }
}