package com.dylanc.retrofit.helper.rxjava

import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * @author Dylan Cai
 * @since 2019/12/17
 */
class LoadingTransformer<T>(
  private val requestLoading: RequestLoading
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>,
  SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {

  override fun apply(upstream: Observable<T>): Observable<T> =
    upstream.doOnSubscribe { requestLoading.show() }.doFinally { requestLoading.dismiss() }

  override fun apply(upstream: Flowable<T>): Publisher<T> =
    upstream.doOnSubscribe { requestLoading.show() }.doFinally { requestLoading.dismiss() }

  override fun apply(upstream: Single<T>): SingleSource<T> =
    upstream.doOnSubscribe { requestLoading.show() }.doFinally { requestLoading.dismiss() }

  override fun apply(upstream: Maybe<T>): MaybeSource<T> =
    upstream.doOnSubscribe { requestLoading.show() }.doFinally { requestLoading.dismiss() }

  override fun apply(upstream: Completable): CompletableSource =
    upstream.doOnSubscribe { requestLoading.show() }.doFinally { requestLoading.dismiss() }
}