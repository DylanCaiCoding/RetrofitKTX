package com.dylanc.retrofit.helper.transformer

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.ProgressManager
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
fun Observable<ResponseBody>.toFile(pathname: String): Observable<File> =
  compose(DownloadTransformer.toFile(pathname))

fun Observable<ResponseBody>.addDownloadListener(
  url: String,
  progressListener: ProgressListener
): Observable<ResponseBody> =
  compose(DownloadTransformer.addProgressListener(url, progressListener))

fun Observable<ResponseBody>.addDiffDownloadListenerOnSameUrl(
  url: String,
  progressListener: ProgressListener
): Observable<ResponseBody> =
  compose(DownloadTransformer.addDiffProgressListenerOnSameUrl(url, progressListener))

fun Observable<ResponseBody>.addDiffDownloadListenerOnSameUrl(
  url: String,
  key: String,
  progressListener: ProgressListener
): Observable<ResponseBody> =
  compose(DownloadTransformer.addDiffProgressListenerOnSameUrl(url,key, progressListener))

object DownloadTransformer {

  @JvmStatic
  fun addProgressListener(
    url: String,
    progressListener: ProgressListener
  ): ObservableTransformer<ResponseBody, ResponseBody> {
    return ObservableTransformer { upstream ->
      ProgressManager.getInstance().addResponseListener(url, progressListener)
      upstream
    }
  }

  @JvmStatic
  fun addDiffProgressListenerOnSameUrl(
    url: String,
    progressListener: ProgressListener
  ): ObservableTransformer<ResponseBody, ResponseBody> {
    return ObservableTransformer { upstream ->
      ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, progressListener)
      upstream
    }
  }

  @JvmStatic
  fun addDiffProgressListenerOnSameUrl(
    url: String,
    key: String,
    progressListener: ProgressListener
  ): ObservableTransformer<ResponseBody, ResponseBody> {
    return ObservableTransformer { upstream ->
      ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, key, progressListener)
      upstream
    }
  }

  @JvmStatic
  fun toFile(pathname: String): ObservableTransformer<ResponseBody, File> {
    return ObservableTransformer { upstream ->
      upstream.subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .map { responseBody ->
          responseBody.byteStream()
        }
        .observeOn(Schedulers.computation())
        .map { inputStream ->
          val file = File(pathname)
          inputStream.use { input ->
            file.outputStream().use { fileOut ->
              input.copyTo(fileOut)
            }
          }
          file
        }
        .observeOn(AndroidSchedulers.mainThread())
    }
  }
}