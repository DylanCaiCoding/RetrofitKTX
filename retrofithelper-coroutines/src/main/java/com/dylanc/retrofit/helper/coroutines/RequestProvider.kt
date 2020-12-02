package com.dylanc.retrofit.helper.coroutines

private val map = hashMapOf<String, ViewModelRequest>()

class RequestProvider() {

  fun get(clazz: Class<ViewModelRequest>): ViewModelRequest? {
//    if (map[clazz.name] == null) {
////      map[clazz.name] =
//    }
    return map[clazz.name]!!
  }

  internal fun clear(){

  }
}