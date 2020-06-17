package com.dylanc.retrofit.helper.sample.network

import androidx.lifecycle.*
import com.dylanc.retrofit.helper.coroutines.onRequestSuccessListener
import java.lang.Exception

sealed class RequestState<out T> {
  data class Loading<out T>(val isLoading: Boolean) : RequestState<T>()
  data class Success<out T>(val data: T?) : RequestState<T>()
  data class Error<out T>(val error: Exception) : RequestState<T>()
}

typealias RequestStateLiveData<T> = LiveData<RequestState<T>>

fun <T> loadingRequestState(isLoading: Boolean = true) =
  RequestState.Loading<T>(isLoading)

suspend fun <T : Any> requestState(block: suspend () -> T): RequestState<T> = try {
  RequestState.Success(block().also {
    onRequestSuccessListener?.invoke(it)
  })
} catch (e: Exception) {
  RequestState.Error(e)
}

fun <T> RequestStateLiveData<T>.observeRequestState(
  lifecycleOwner: LifecycleOwner,
  onLoading: (Boolean) -> Unit = {},
  onSuccess: (T?) -> Unit = {},
  onError: (Exception) -> Unit = {}
) =
  observe(lifecycleOwner, Observer { state ->
    when (state) {
      is RequestState.Loading -> onLoading(state.isLoading)
      is RequestState.Success -> onSuccess(state.data)
      is RequestState.Error -> onError(state.error)
    }
  })