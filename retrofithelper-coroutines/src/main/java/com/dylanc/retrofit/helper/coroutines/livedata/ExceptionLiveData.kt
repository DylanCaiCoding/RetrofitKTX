@file:Suppress("unused")

package com.dylanc.retrofit.helper.coroutines.livedata

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch


typealias ExceptionLiveData = EventLiveData<Throwable>

fun <T> Flow<T>.catch(exception: ExceptionLiveData) =
  catch { exception.value = it }
