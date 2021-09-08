package com.dylanc.retrofit.rxjava

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * @author Dylan Cai
 */
class LoadingTransformer<T>(
  private val requestLoading: RequestLoading
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>,
  SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {

  override fun apply(upstream: Observable<T>): Observable<T> =
    upstream.doOnSubscribe { requestLoading.onRequestLoading(true) }.doFinally { requestLoading.onRequestLoading(false) }

  override fun apply(upstream: Flowable<T>): Publisher<T> =
    upstream.doOnSubscribe { requestLoading.onRequestLoading(true) }.doFinally { requestLoading.onRequestLoading(false) }

  override fun apply(upstream: Single<T>): SingleSource<T> =
    upstream.doOnSubscribe { requestLoading.onRequestLoading(true) }.doFinally { requestLoading.onRequestLoading(false) }

  override fun apply(upstream: Maybe<T>): MaybeSource<T> =
    upstream.doOnSubscribe { requestLoading.onRequestLoading(true) }.doFinally { requestLoading.onRequestLoading(false) }

  override fun apply(upstream: Completable): CompletableSource =
    upstream.doOnSubscribe { requestLoading.onRequestLoading(true) }.doFinally { requestLoading.onRequestLoading(false) }
}
