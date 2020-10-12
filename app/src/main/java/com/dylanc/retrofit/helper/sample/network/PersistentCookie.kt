@file:Suppress("unused")
@file:JvmName("PersistentCookie")

package com.dylanc.retrofit.helper.sample.network

import android.content.Context
import com.dylanc.retrofit.helper.RetrofitHelper
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient

private lateinit var persistentCookieJar: PersistentCookieJar

fun OkHttpClient.Builder.persistentCookies(context: Context) =
  cookieJar(persistentCookieJarOf(context))

fun RetrofitHelper.Builder.persistentCookies(context: Context) =
  cookieJar(persistentCookieJarOf(context))

@JvmName("create")
fun persistentCookieJarOf(context: Context): PersistentCookieJar {
  persistentCookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
  return persistentCookieJar
}

@JvmName("clear")
fun clearPersistentCookies() {
  persistentCookieJar.clear()
}