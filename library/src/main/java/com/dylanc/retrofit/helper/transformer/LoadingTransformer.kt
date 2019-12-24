package com.dylanc.retrofit.helper.transformer

import android.content.Context
import io.reactivex.*
import org.reactivestreams.Publisher

/**
 * @author Dylan Cai
 * @since 2019/12/17
 */
class LoadingTransformer<T>(
  private val context: Context,
  private val onStart: (context: Context) -> Unit,
  private val onEnd: () -> Unit
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>,
  SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {

  override fun apply(upstream: Observable<T>): Observable<T> =
    upstream.doOnSubscribe { onStart.invoke(context) }.doOnTerminate(onEnd)

  override fun apply(upstream: Flowable<T>): Publisher<T> =
    upstream.doOnSubscribe { onStart.invoke(context) }.doOnTerminate(onEnd)

  override fun apply(upstream: Single<T>): SingleSource<T> =
    upstream.doOnSubscribe { onStart.invoke(context) }.doAfterTerminate(onEnd)

  override fun apply(upstream: Maybe<T>): MaybeSource<T> =
    upstream.doOnSubscribe { onStart.invoke(context) }.doAfterTerminate(onEnd)

  override fun apply(upstream: Completable): CompletableSource =
    upstream.doOnSubscribe { onStart.invoke(context) }.doOnTerminate(onEnd)
}