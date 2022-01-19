@file:Suppress("unused")

package com.dylanc.retrofit.coroutines

import kotlinx.coroutines.flow.*

fun <T> Flow<T>.showLoadingWith(flow: MutableSharedFlow<Boolean>) =
  onStart { flow.emit(true) }
    .onCompletion { flow.emit(false) }

fun <T> Flow<T>.catchWith(flow: MutableSharedFlow<Throwable>) =
  catch { flow.emit(it) }
