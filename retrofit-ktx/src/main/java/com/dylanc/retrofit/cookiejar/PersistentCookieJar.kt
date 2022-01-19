@file:Suppress("unused")

package com.dylanc.retrofit.cookiejar

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.dylanc.retrofit.cookiejar.PersistentCookieJar.IdentifiableCookie.Companion.decorateAll
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.internal.and
import java.io.*
import java.util.*

fun OkHttpClient.Builder.persistentCookieJar(context: Context) =
  cookieJar(PersistentCookieJarFactory.create(context))

fun clearPersistentCookieJar() = PersistentCookieJarFactory.clear()

object PersistentCookieJarFactory {
  private lateinit var persistentCookieJar: PersistentCookieJar

  @JvmStatic
  fun create(context: Context) = PersistentCookieJar(context).also { persistentCookieJar = it }

  @JvmStatic
  fun clear() = persistentCookieJar.clear()
}

class PersistentCookieJar(private val sharedPreferences: SharedPreferences) : CookieJar {

  private val cookies: MutableSet<IdentifiableCookie> = HashSet()

  private val iterator: MutableIterator<IdentifiableCookie> = cookies.iterator()

  constructor(context: Context) :
      this(context.getSharedPreferences("CookiePersistence", Context.MODE_PRIVATE))

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

  private class IdentifiableCookie(val cookie: Cookie) {
    override fun equals(other: Any?): Boolean =
      other is IdentifiableCookie && other.cookie.name == cookie.name && other.cookie.domain == cookie.domain &&
          other.cookie.path == cookie.path && other.cookie.secure == cookie.secure && other.cookie.hostOnly == cookie.hostOnly

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
      fun decorateAll(cookies: Collection<Cookie>): List<IdentifiableCookie> =
        cookies.map { IdentifiableCookie(it) }
    }
  }
}

class SerializableCookie : Serializable {
  @Transient
  private var cookie: Cookie? = null

  fun encode(cookie: Cookie?): String? {
    this.cookie = cookie
    val byteArrayOutputStream = ByteArrayOutputStream()
    var objectOutputStream: ObjectOutputStream? = null
    try {
      objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
      objectOutputStream.writeObject(this)
    } catch (e: IOException) {
      Log.d(TAG, "IOException in encodeCookie", e)
      return null
    } finally {
      if (objectOutputStream != null) {
        try {
          // Closing a ByteArrayOutputStream has no effect, it can be used later (and is used in the return statement)
          objectOutputStream.close()
        } catch (e: IOException) {
          Log.d(TAG, "Stream not closed in encodeCookie", e)
        }
      }
    }
    return byteArrayToHexString(byteArrayOutputStream.toByteArray())
  }

  fun decode(encodedCookie: String): Cookie? {
    val bytes = hexStringToByteArray(encodedCookie)
    val byteArrayInputStream = ByteArrayInputStream(
      bytes
    )
    var cookie: Cookie? = null
    var objectInputStream: ObjectInputStream? = null
    try {
      objectInputStream = ObjectInputStream(byteArrayInputStream)
      cookie = (objectInputStream.readObject() as SerializableCookie).cookie
    } catch (e: IOException) {
      Log.d(TAG, "IOException in decodeCookie", e)
    } catch (e: ClassNotFoundException) {
      Log.d(TAG, "ClassNotFoundException in decodeCookie", e)
    } finally {
      if (objectInputStream != null) {
        try {
          objectInputStream.close()
        } catch (e: IOException) {
          Log.d(TAG, "Stream not closed in decodeCookie", e)
        }
      }
    }
    return cookie
  }

  @Throws(IOException::class)
  private fun writeObject(out: ObjectOutputStream) {
    out.writeObject(cookie!!.name)
    out.writeObject(cookie!!.value)
    out.writeLong(if (cookie!!.persistent) cookie!!.expiresAt else NON_VALID_EXPIRES_AT)
    out.writeObject(cookie!!.domain)
    out.writeObject(cookie!!.path)
    out.writeBoolean(cookie!!.secure)
    out.writeBoolean(cookie!!.httpOnly)
    out.writeBoolean(cookie!!.hostOnly)
  }

  @Throws(IOException::class, ClassNotFoundException::class)
  private fun readObject(`in`: ObjectInputStream) {
    val builder = Cookie.Builder()
    builder.name((`in`.readObject() as String))
    builder.value((`in`.readObject() as String))
    val expiresAt = `in`.readLong()
    if (expiresAt != NON_VALID_EXPIRES_AT) {
      builder.expiresAt(expiresAt)
    }
    val domain = `in`.readObject() as String
    builder.domain(domain)
    builder.path((`in`.readObject() as String))
    if (`in`.readBoolean()) builder.secure()
    if (`in`.readBoolean()) builder.httpOnly()
    if (`in`.readBoolean()) builder.hostOnlyDomain(domain)
    cookie = builder.build()
  }

  companion object {
    private val TAG = SerializableCookie::class.java.simpleName
    private const val serialVersionUID = -8594045714036645534L

    /**
     * Using some super basic byte array &lt;-&gt; hex conversions so we don't
     * have to rely on any large Base64 libraries. Can be overridden if you
     * like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    private fun byteArrayToHexString(bytes: ByteArray): String {
      val sb = StringBuilder(bytes.size * 2)
      for (element in bytes) {
        val v: Int = element and 0xff
        if (v < 16) {
          sb.append('0')
        }
        sb.append(Integer.toHexString(v))
      }
      return sb.toString()
    }

    /**
     * Converts hex values from strings to byte array
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    private fun hexStringToByteArray(hexString: String): ByteArray {
      val len = hexString.length
      val data = ByteArray(len / 2)
      var i = 0
      while (i < len) {
        data[i / 2] = ((Character.digit(hexString[i], 16) shl 4) + Character
          .digit(hexString[i + 1], 16)).toByte()
        i += 2
      }
      return data
    }

    private const val NON_VALID_EXPIRES_AT = -1L
  }
}
