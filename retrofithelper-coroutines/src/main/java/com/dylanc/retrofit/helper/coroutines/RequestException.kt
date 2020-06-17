package com.dylanc.retrofit.helper.coroutines

data class RequestException(
  override val cause: Throwable,
  override val message: String? = cause.message,
  val requestToken: Any?= null,
  val loadingType: Any?= null
) : Throwable(message, cause)