@file:Suppress("unused")

package com.dylanc.retrofit.helper.rxjava

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.*

/**
 * @author Dylan Cai
 */

fun <T> Observable<T>.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): ObservableSubscribeProxy<T> =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

fun <T> Flowable<T>.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): FlowableSubscribeProxy<T> =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

fun <T> Single<T>.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): SingleSubscribeProxy<T> =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

fun <T> Maybe<T>.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): MaybeSubscribeProxy<T> =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

fun Completable.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): CompletableSubscribeProxy =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))