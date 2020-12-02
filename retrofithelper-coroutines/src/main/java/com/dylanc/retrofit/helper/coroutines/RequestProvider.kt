package com.dylanc.retrofit.helper.coroutines

private val map = hashMapOf<String, Request>()

class RequestProvider(requestViewModel: RequestViewModel) {

  fun get(clazz: Class<Request>): Request? {
//    if (map[clazz.name] == null) {
////      map[clazz.name] =
//    }
    return map[clazz.name]!!
  }

  internal fun clear(){

  }
}