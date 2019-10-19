package com.dylanc.retrofit.helper.transformer

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.ProgressManager
import okhttp3.ResponseBody

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
fun <T> Observable<T>.addUploadListener(
  url: String,
  progressListener: ProgressListener
): Observable<T> =
  compose(UploadTransformer.addProgressListener(url, progressListener))

fun <T> Observable<T>.addDiffUploadListenerOnSameUrl(
  url: String,
  progressListener: ProgressListener
): Observable<T> =
  compose(UploadTransformer.addDiffProgressListenerOnSameUrl(url, progressListener))

fun <T> Observable<T>.addDiffUploadListenerOnSameUrl(
  url: String,
  key: String,
  progressListener: ProgressListener
): Observable<T> =
  compose(UploadTransformer.addDiffProgressListenerOnSameUrl(url, key, progressListener))

object UploadTransformer {

  @JvmStatic
  fun <T> addProgressListener(
    url: String,
    progressListener: ProgressListener
  ): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      ProgressManager.getInstance().addRequestListener(url, progressListener)
      upstream
    }
  }

  @JvmStatic
  fun <T> addDiffProgressListenerOnSameUrl(
    url: String,
    progressListener: ProgressListener
  ): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, progressListener)
      upstream
    }
  }

  @JvmStatic
  fun <T> addDiffProgressListenerOnSameUrl(
    url: String,
    key: String,
    progressListener: ProgressListener
  ): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, key, progressListener)
      upstream
    }
  }
}