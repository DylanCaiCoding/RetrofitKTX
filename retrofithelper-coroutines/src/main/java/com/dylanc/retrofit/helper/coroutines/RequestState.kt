package com.dylanc.retrofit.helper.coroutines


suspend fun <T : Any> requestState(
  responseHandler: (T) -> Unit = {},
  block: suspend () -> T
): RequestState<T> =
  try {
    RequestState.Success(block().apply(responseHandler))
  } catch (e: Exception) {
    RequestState.Error(e)
  }

sealed class RequestState<out T> {
  data class Loading<out T>(val isLoading: Boolean) : RequestState<T>()
  data class Success<out T>(val data: T?) : RequestState<T>()
  data class Error<out T>(val error: Exception) : RequestState<T>()
}