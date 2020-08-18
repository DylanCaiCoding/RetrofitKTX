package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.*
import kotlin.experimental.ExperimentalTypeInference


typealias ResultLiveData<T> = LiveData<Result<T>>

@Suppress("RemoveExplicitTypeArguments")
@OptIn(ExperimentalTypeInference::class)
fun <T> resultLiveData(@BuilderInference block: suspend LiveDataScope<Result<T>>.() -> Unit) =
  liveData<Result<T>> {
    emit(Result.Loading(true))
    block()
    emit(Result.Loading(false))
  }

fun <T> ResultLiveData<T>.observeResult(
  lifecycleOwner: LifecycleOwner,
  onLoading: (Boolean) -> Unit = {},
  onSuccess: (T?) -> Unit = {},
  onError: (Throwable) -> Unit = {}
) =
  observe(lifecycleOwner, Observer { state ->
    when (state) {
      is Result.Loading -> onLoading(state.isLoading)
      is Result.Success -> onSuccess(state.data)
      is Result.Error -> if (state.error is RequestException) {
        onError(state.error.cause)
      } else {
        onError(state.error)
      }
    }
  })