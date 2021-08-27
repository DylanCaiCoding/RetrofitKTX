@file:Suppress("unused", "NOTHING_TO_INLINE")
@file:JvmName("PersistentCookie")

package com.dylanc.retrofit.cookie

import android.content.Context
import android.content.SharedPreferences
import com.dylanc.retrofit.app.application
import com.dylanc.retrofit.cookie.PersistentCookieJar.IdentifiableCookie.Companion.decorateAll
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import java.util.*


private lateinit var persistentCookieJar: PersistentCookieJar

inline fun OkHttpClient.Builder.persistentCookies() = cookieJar(PersistentCookieJar())

@JvmName("create")
fun PersistentCookieJar(): PersistentCookieJar {
  persistentCookieJar = PersistentCookieJar(application)
  return persistentCookieJar
}

@JvmName("clear")
fun clearPersistentCookies() {
  persistentCookieJar.clear()
}

class PersistentCookieJar(private val sharedPreferences: SharedPreferences) : CookieJar {

  private val cookies: MutableSet<IdentifiableCookie> = HashSet()

  private val iterator: MutableIterator<IdentifiableCookie> = cookies.iterator()

  constructor(context: Context) : this(
    context.getSharedPreferences("CookiePersistence", Context.MODE_PRIVATE)
  )

  init {
    cookies.addAll(sharedPreferences.loadAll())
  }

  @Synchronized
  override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
    this.cookies.addAll(cookies)
    sharedPreferences.saveAll(filterPersistentCookies(cookies))
  }

  @Synchronized
  override fun loadForRequest(url: HttpUrl): List<Cookie> {
    val cookiesToRemove: MutableList<Cookie> = ArrayList()
    val validCookies: MutableList<Cookie> = ArrayList()
    val it = cookies.iterator()
    while (it.hasNext()) {
      val currentCookie = it.next().cookie
      if (isCookieExpired(currentCookie)) {
        cookiesToRemove.add(currentCookie)
        it.remove()
      } else if (currentCookie.matches(url)) {
        validCookies.add(currentCookie)
      }
    }
    sharedPreferences.removeAll(cookiesToRemove)
    return validCookies
  }

  @Synchronized
  fun clearSession() {
    cookies.clear()
    cookies.addAll(sharedPreferences.loadAll())
  }

  @Synchronized
  fun clear() {
    cookies.clear()
    sharedPreferences.clear()
  }


  private fun MutableSet<IdentifiableCookie>.addAll(cookies: Collection<Cookie>) {
    for (cookie in decorateAll(cookies)) {
      remove(cookie)
      add(cookie)
    }
  }

  private fun SharedPreferences.loadAll(): List<Cookie> {
    val cookies: MutableList<Cookie> = ArrayList(all.size)
    for ((_, value) in all) {
      val serializedCookie = value as String
      val cookie = SerializableCookie().decode(serializedCookie)
      if (cookie != null) {
        cookies.add(cookie)
      }
    }
    return cookies
  }

  private fun SharedPreferences.saveAll(cookies: Collection<Cookie>) {
    val editor = edit()
    for (cookie in cookies) {
      editor.putString(createCookieKey(cookie), SerializableCookie().encode(cookie))
    }
    editor.apply()
  }

  private fun SharedPreferences.removeAll(cookies: Collection<Cookie>) {
    val editor = edit()
    for (cookie in cookies) {
      editor.remove(createCookieKey(cookie))
    }
    editor.apply()
  }

  private fun SharedPreferences.clear() {
    edit().clear().apply()
  }

  companion object {
    private fun filterPersistentCookies(cookies: List<Cookie>): List<Cookie> {
      val persistentCookies: MutableList<Cookie> = ArrayList()
      for (cookie in cookies) {
        if (cookie.persistent) {
          persistentCookies.add(cookie)
        }
      }
      return persistentCookies
    }

    private fun isCookieExpired(cookie: Cookie): Boolean {
      return cookie.expiresAt < System.currentTimeMillis()
    }

    private fun createCookieKey(cookie: Cookie): String {
      return (if (cookie.secure) "https" else "http") + "://" + cookie.domain + cookie.path + "|" + cookie.name
    }
  }

  private class IdentifiableCookie(val cookie: Cookie) {
    override fun equals(other: Any?): Boolean {
      if (other !is IdentifiableCookie) return false
      return other.cookie.name == cookie.name && other.cookie.domain == cookie.domain && other.cookie.path == cookie.path && other.cookie.secure == cookie.secure && other.cookie.hostOnly == cookie.hostOnly
    }

    override fun hashCode(): Int {
      var hash = 17
      hash = 31 * hash + cookie.name.hashCode()
      hash = 31 * hash + cookie.domain.hashCode()
      hash = 31 * hash + cookie.path.hashCode()
      hash = 31 * hash + if (cookie.secure) 0 else 1
      hash = 31 * hash + if (cookie.hostOnly) 0 else 1
      return hash
    }

    companion object {
      @JvmStatic
      fun decorateAll(cookies: Collection<Cookie>): List<IdentifiableCookie> {
        val identifiableCookies: MutableList<IdentifiableCookie> = ArrayList(cookies.size)
        for (cookie in cookies) {
          identifiableCookies.add(IdentifiableCookie(cookie))
        }
        return identifiableCookies
      }
    }
  }
}