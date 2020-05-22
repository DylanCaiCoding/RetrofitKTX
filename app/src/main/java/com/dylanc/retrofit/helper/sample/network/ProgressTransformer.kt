package com.dylanc.retrofit.helper.sample.network

import io.reactivex.*
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.body.ProgressInfo
import org.reactivestreams.Publisher
import java.lang.Exception

/**
 * @author Dylan Cai
 * @since 2019/12/17
 */


class ProgressTransformer<T>(
  private val onProgress: (progressInfo: ProgressInfo) -> Unit,
  private val onError: (id: Long, e: Exception) -> Unit,
  private val observer: (listener: ProgressListener) -> Unit
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>,
  MaybeTransformer<T, T>, CompletableTransformer {

  override fun apply(upstream: Observable<T>): ObservableSource<T> = upstream
    .doOnSubscribe { observer.invoke(listener) }

  override fun apply(upstream: Flowable<T>): Publisher<T> = upstream
    .doOnSubscribe { observer.invoke(listener) }

  override fun apply(upstream: Single<T>): SingleSource<T> = upstream
    .doOnSubscribe { observer.invoke(listener) }

  override fun apply(upstream: Maybe<T>): MaybeSource<T> = upstream
    .doOnSubscribe { observer.invoke(listener) }

  override fun apply(upstream: Completable): CompletableSource = upstream
    .doOnSubscribe { observer.invoke(listener) }

  private val listener = object : ProgressListener {

    override fun onProgress(progressInfo: ProgressInfo) {
      onProgress.invoke(progressInfo)
    }

    override fun onError(id: Long, e: Exception) {
      onError.invoke(id, e)
    }
  }
}