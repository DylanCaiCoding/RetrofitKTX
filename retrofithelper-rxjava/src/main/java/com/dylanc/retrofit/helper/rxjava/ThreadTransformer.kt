package com.dylanc.retrofit.helper.rxjava

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * @author Dylan Cai
 * @since 2019/12/17
 */
class ThreadTransformer<T>(
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