@file:Suppress("unused")

package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

suspend fun <T : Any> request(responseHandler: (T) -> Unit = {}, block: suspend () -> T): T =
  request(null, responseHandler, block)

suspend fun <T : Any> request(
  requestToken: Any?,
  responseHandler: (T) -> Unit = {},
  block: suspend () -> T
): T =
  try {
    block().apply(responseHandler)
  } catch (e: Exception) {
    throw RequestException(e, e.message, requestToken)
  }

class RequestExceptionHandler : AbstractCoroutineContextElement(CoroutineExceptionHandler),
  CoroutineExceptionHandler {
  private val _requestException: MutableLiveData<RequestException> = MutableLiveData()
  val requestException: LiveData<RequestException> = _requestException

  override fun handleException(context: CoroutineContext, exception: Throwable) {
    val requestException = if (exception is RequestException) {
      exception
    } else {
      RequestException(exception)
    }
    _requestException.postValue(requestException)
  }
}