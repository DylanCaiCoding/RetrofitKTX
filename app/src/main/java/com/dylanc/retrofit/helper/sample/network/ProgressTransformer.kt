@file:Suppress("unused")

package com.dylanc.retrofit.helper.sample.network

import com.dylanc.retrofit.helper.Default
import io.reactivex.*
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.progressmanager.body.ProgressInfo
import org.reactivestreams.Publisher

/**
 * @author Dylan Cai
 * @since 2019/12/17
 */
fun Default.observeProgress() =
  okHttpClientBuilder {
    ProgressManager.getInstance().with(this)
  }

fun <T> Observable<T>.observeDownload(
  url: String,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Observable<T> =
  compose(ProgressTransformer.observeDownload(url, onProgress, onError))

fun <T> Observable<T>.observeDiffDownloadOnSameUrl(
  url: String,
  key: String? = null,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Observable<T> =
  compose(ProgressTransformer.observeDiffDownloadOnSameUrl(url, key, onProgress, onError))

fun <T> Observable<T>.observeUpload(
  url: String,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Observable<T> =
  compose(ProgressTransformer.observeUpload(url, onProgress, onError))

fun <T> Observable<T>.observeDiffUploadOnSameUrl(
  url: String,
  key: String? = null,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Observable<T> =
  compose(ProgressTransformer.observeDiffUploadOnSameUrl(url, key, onProgress, onError))

fun <T> Flowable<T>.observeDownload(
  url: String,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Flowable<T> =
  compose(ProgressTransformer.observeDownload(url, onProgress, onError))

fun <T> Flowable<T>.observeDiffDownloadOnSameUrl(
  url: String,
  key: String? = null,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Flowable<T> =
  compose(ProgressTransformer.observeDiffDownloadOnSameUrl(url, key, onProgress, onError))

fun <T> Flowable<T>.observeUpload(
  url: String,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Flowable<T> =
  compose(ProgressTransformer.observeUpload(url, onProgress, onError))

fun <T> Flowable<T>.observeDiffUploadOnSameUrl(
  url: String,
  key: String? = null,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Flowable<T> =
  compose(ProgressTransformer.observeDiffUploadOnSameUrl(url, key, onProgress, onError))

fun <T> Single<T>.observeDownload(
  url: String,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Single<T> =
  compose(ProgressTransformer.observeDownload(url, onProgress, onError))

fun <T> Single<T>.observeDiffDownloadOnSameUrl(
  url: String,
  key: String? = null,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Single<T> =
  compose(ProgressTransformer.observeDiffDownloadOnSameUrl(url, key, onProgress, onError))

fun <T> Single<T>.observeUpload(
  url: String,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Single<T> =
  compose(ProgressTransformer.observeUpload(url, onProgress, onError))

fun <T> Single<T>.observeDiffUploadOnSameUrl(
  url: String,
  key: String? = null,
  onProgress: (progressInfo: ProgressInfo) -> Unit,
  onError: (id: Long, e: Exception) -> Unit
): Single<T> =
  compose(ProgressTransformer.observeDiffUploadOnSameUrl(url, key, onProgress, onError))

class ProgressTransformer<T>(
  private val onProgress: (progressInfo: ProgressInfo) -> Unit,
  private val onError: (id: Long, e: Exception) -> Unit,
  private val observer: (listener: ProgressListener) -> Unit
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>,
  MaybeTransformer<T, T>, CompletableTransformer {

  companion object {
    @JvmStatic
    fun <T> observeDownload(
      url: String,
      onProgress: (progressInfo: ProgressInfo) -> Unit,
      onError: (id: Long, e: Exception) -> Unit
    ) = ProgressTransformer<T>(onProgress, onError) { listener ->
      ProgressManager.getInstance().addResponseListener(url, listener)
    }

    @JvmStatic
    fun <T> observeDiffDownloadOnSameUrl(
      url: String,
      key: String? = null,
      onProgress: (progressInfo: ProgressInfo) -> Unit,
      onError: (id: Long, e: Exception) -> Unit
    ) = ProgressTransformer<T>(onProgress, onError) { listener ->
      if (key == null) {
        ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, listener)
      } else {
        ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, key, listener)
      }
    }

    @JvmStatic
    fun <T> observeUpload(
      url: String,
      onProgress: (progressInfo: ProgressInfo) -> Unit,
      onError: (id: Long, e: Exception) -> Unit
    ) = ProgressTransformer<T>(onProgress, onError) { listener ->
      ProgressManager.getInstance().addRequestListener(url, listener)
    }

    @JvmStatic
    fun <T> observeDiffUploadOnSameUrl(
      url: String,
      key: String? = null,
      onProgress: (progressInfo: ProgressInfo) -> Unit,
      onError: (id: Long, e: Exception) -> Unit
    ) = ProgressTransformer<T>(onProgress, onError) { listener ->
      if (key == null) {
        ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, listener)
      } else {
        ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, key, listener)
      }
    }
  }

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