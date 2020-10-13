@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.autodispose

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.*
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.*

/**
 * @author Dylan Cai
 */

inline fun <T> Observable<T>.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): ObservableSubscribeProxy<T> =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

inline fun <T> Flowable<T>.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): FlowableSubscribeProxy<T> =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

inline fun <T> Single<T>.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): SingleSubscribeProxy<T> =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

inline fun <T> Maybe<T>.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): MaybeSubscribeProxy<T> =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

inline fun Completable.autoDispose(
  lifecycleOwner: LifecycleOwner,
  untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
): CompletableSubscribeProxy =
  autoDispose(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))

object AutoDisposable {
  @JvmStatic
  @JvmOverloads
  fun <T> bind(
    lifecycleOwner: LifecycleOwner,
    untilEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
  ): AutoDisposeConverter<T> =
    AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent))
}