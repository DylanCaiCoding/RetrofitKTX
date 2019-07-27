package com.dylanc.retrofit.helper

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.CookieCache
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
class PersistentCookie(cache: CookieCache, persistor: CookiePersistor) : PersistentCookieJar(cache, persistor) {
  constructor(context: Context) : this(SetCookieCache(), SharedPrefsCookiePersistor(context))
}
