@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.coroutines

import kotlinx.coroutines.flow.*

/**
 * @author Dylan Cai
 */

inline fun <T> Flow<T>.showLoadingWith(flow: MutableSharedFlow<Boolean>) =
  onStart { flow.emit(true) }
    .onCompletion { flow.emit(false) }

inline fun <T> Flow<T>.catchWith(flow: MutableSharedFlow<Throwable>) =
  catch { flow.emit(it) }
