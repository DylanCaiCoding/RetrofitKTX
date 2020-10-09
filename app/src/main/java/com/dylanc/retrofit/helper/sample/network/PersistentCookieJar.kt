@file:Suppress("unused")

package com.dylanc.retrofit.helper.sample.network

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient

private lateinit var persistentCookieJar: PersistentCookieJar

fun OkHttpClient.Builder.persistentCookies(context: Context) = apply {
  persistentCookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
  cookieJar(persistentCookieJar)
}

fun clearPersistentCookies() {
  persistentCookieJar.clear()
}