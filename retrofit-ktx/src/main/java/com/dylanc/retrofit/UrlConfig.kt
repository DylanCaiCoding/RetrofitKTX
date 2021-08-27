package com.dylanc.retrofit

/**
 * @author Dylan Cai
 */

internal val baseUrl: String? get() = urlConfigOf("baseUrl")

internal val debugUrl: String? get() = urlConfigOf("debugUrl")

internal fun domains() = lazy {
  urlConfigOf<HashMap<String, String>>("domains") ?: mutableMapOf()
}

@Suppress("UNCHECKED_CAST")
private fun <T> urlConfigOf(fieldName: String): T? = try {
  val clazz = Class.forName("com.dylanc.retrofit.UrlConfig")
  val urlConfig = clazz.newInstance()
  clazz.getField(fieldName)[urlConfig] as T?
} catch (e: NoSuchFieldException) {
  null
} catch (e: Exception) {
  e.printStackTrace()
  null
}