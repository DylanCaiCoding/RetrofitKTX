package com.dylanc.retrofit.helper.coroutines


object RequestProvider {
  private val viewModelRequestStore = hashMapOf<String, ViewModelRequest>()

  @Suppress("UNCHECKED_CAST")
  fun <T : ViewModelRequest> get(clazz: Class<T>): T {
    val qualifiedName = clazz.canonicalName!!
    if (viewModelRequestStore[qualifiedName] == null) {
      viewModelRequestStore[qualifiedName] = clazz.newInstance()
    }
    return viewModelRequestStore[qualifiedName] as T
  }
}