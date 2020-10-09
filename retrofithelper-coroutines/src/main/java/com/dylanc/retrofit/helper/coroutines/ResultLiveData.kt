@file:Suppress("unused")

package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.*
import kotlin.experimental.ExperimentalTypeInference


typealias ResultLiveData<T> = LiveData<Result<T>>

fun <T> ResultLiveData<T>.observeResult(
  lifecycleOwner: LifecycleOwner,
  onSuccess: (T?) -> Unit = {},
  onError: (Throwable) -> Unit = {}
) =
  observe(lifecycleOwner, Observer { state ->
    when (state) {
      is Result.Success -> onSuccess(state.data)
      is Result.Error -> onError(state.error)
    }
  })