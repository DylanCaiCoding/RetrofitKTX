package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import kotlin.experimental.ExperimentalTypeInference


typealias RequestStateLiveData<T> = LiveData<RequestState<T>>

@OptIn(ExperimentalTypeInference::class)
fun <T> requestStateLiveData(@BuilderInference block: suspend LiveDataScope<RequestState<T>>.() -> Unit) =
  liveData<RequestState<T>> {
    emit(RequestState.Loading(true))
    block()
    emit(RequestState.Loading(false))
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