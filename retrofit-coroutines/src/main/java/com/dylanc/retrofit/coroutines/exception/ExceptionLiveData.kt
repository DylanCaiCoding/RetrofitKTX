@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.coroutines.exception

import com.dylanc.retrofit.coroutines.RequestLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch


typealias ExceptionLiveData = RequestLiveData<Throwable>

inline fun <T> Flow<T>.catch(exception: ExceptionLiveData) =
  catch { exception.value = it }
