package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart


typealias LoadingLiveData = MutableLiveData<Boolean>
typealias ExceptionLiveData = MutableLiveData<Throwable>

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.showLoading(isLoading: LoadingLiveData) =
  onStart { isLoading.value = true }
    .onCompletion { isLoading.value = false }

fun <T> Flow<T>.catch(exception: ExceptionLiveData) =
  catch { exception.value = it }
