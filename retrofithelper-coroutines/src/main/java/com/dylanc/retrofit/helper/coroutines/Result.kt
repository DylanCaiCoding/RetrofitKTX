package com.dylanc.retrofit.helper.coroutines


suspend fun <T : Any> result(
  responseHandler: (T) -> Unit = {},
  block: suspend () -> T
): Result<T> =
  try {
    Result.Success(block().apply(responseHandler))
  } catch (e: Exception) {
    Result.Error(e)
  }

sealed class Result<out T> {
  data class Success<out T>(val data: T?) : Result<T>()
  data class Error(val error: Throwable) : Result<Nothing>()
}