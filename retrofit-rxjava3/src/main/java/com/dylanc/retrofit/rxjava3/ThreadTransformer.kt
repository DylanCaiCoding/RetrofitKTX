package com.dylanc.retrofit.rxjava3

import io.reactivex.rxjava3.core.*
import org.reactivestreams.Publisher

/**
 * @author Dylan Cai
 */
class ThreadTransformer<T : Any>(
  private val subscribeScheduler: Scheduler,
  private val observeScheduler: Scheduler
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>,
  SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {

  override fun apply(upstream: Observable<T>): ObservableSource<T> = upstream
    .subscribeOn(subscribeScheduler)
    .observeOn(observeScheduler)

  override fun apply(upstream: Flowable<T>): Publisher<T> = upstream
    .subscribeOn(subscribeScheduler)
    .observeOn(observeScheduler)

  override fun apply(upstream: Single<T>): SingleSource<T> = upstream
    .subscribeOn(subscribeScheduler)
    .observeOn(observeScheduler)

  override fun apply(upstream: Maybe<T>): MaybeSource<T> = upstream
    .subscribeOn(subscribeScheduler)
    .observeOn(observeScheduler)

  override fun apply(upstream: Completable): CompletableSource = upstream
    .subscribeOn(subscribeScheduler)
    .observeOn(observeScheduler)
}
