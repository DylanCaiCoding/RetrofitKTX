package com.dylanc.retrofit.helper.coroutines


var onRequestSuccessListener: ((Any) -> Unit)? = null
  private set

object RequestSuccessHandler {
  fun observe(onRequestSuccess: (Any) -> Unit) {
    onRequestSuccessListener = onRequestSuccess
  }
}