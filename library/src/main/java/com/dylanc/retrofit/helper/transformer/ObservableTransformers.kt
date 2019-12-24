package com.dylanc.retrofit.helper.transformer

import android.content.Context
import com.dylanc.retrofit.helper.RequestLoading
import com.dylanc.retrofit.helper.RetrofitHelper
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
 * @since 2019/11/12
 */
fun <T> Observable<T>.io2mainThread(): Observable<T> =
  compose(ObservableTransformers.io2mainThread())

fun <T> Observable<T>.showLoading(
  context: Context,
  requestLoading: RequestLoading = RetrofitHelper.getDefault().requestLoading!!
): Observable<T> = compose(ObservableTransformers.showLoading(context, requestLoading))

fun Observable<ResponseBody>.toFile(pathname: String): Observable<File> =
  compose(ObservableTransformers.toFile(pathname))

fun Observable<ResponseBody>.addDownloadListener(
  url: String,
  progressListener: ProgressListener
): Observable<ResponseBody> =
  compose(ObservableTransformers.addDownloadListener(url, progressListener))

fun Observable<ResponseBody>.addDiffDownloadListenerOnSameUrl(
  url: String,
  key: String? = null,
  progressListener: ProgressListener
): Observable<ResponseBody> =
  compose(ObservableTransformers.addDiffDownloadListenerOnSameUrl(url, key, progressListener))

fun <T> Observable<T>.addUploadListener(
  url: String,
  progressListener: ProgressListener
): Observable<T> =
  compose(ObservableTransformers.addUploadListener(url, progressListener))

fun <T> Observable<T>.addDiffUploadListenerOnSameUrl(
  url: String,
  key: String? = null,
  progressListener: ProgressListener
): Observable<T> =
  compose(ObservableTransformers.addDiffUploadListenerOnSameUrl(url, key, progressListener))

object ObservableTransformers {

  @JvmStatic
  fun <T> io2mainThread(): ObservableTransformer<T, T> = ObservableTransformer { upstream ->
    upstream.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  @JvmOverloads
  @JvmStatic
  fun <T> showLoading(
    context: Context,
    requestLoading: RequestLoading = RetrofitHelper.getDefault().requestLoading!!
  ): ObservableTransformer<T, T> = ObservableTransformer { upstream ->
    upstream.doOnSubscribe {
      requestLoading.show(context)
    }.doOnTerminate {
      requestLoading.dismiss()
    }
  }

  @JvmStatic
  fun addDownloadListener(
    url: String,
    progressListener: ProgressListener
  ): ObservableTransformer<ResponseBody, ResponseBody> = ObservableTransformer { upstream ->
    ProgressManager.getInstance().addResponseListener(url, progressListener)
    upstream
  }

  @JvmOverloads
  @JvmStatic
  fun addDiffDownloadListenerOnSameUrl(
    url: String,
    key: String? = null,
    progressListener: ProgressListener
  ): ObservableTransformer<ResponseBody, ResponseBody> = ObservableTransformer { upstream ->
    if (key == null) {
      ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, progressListener)
    } else {
      ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, key, progressListener)
    }
    upstream
  }

  @JvmStatic
  fun toFile(pathname: String): ObservableTransformer<ResponseBody, File> =
    ObservableTransformer { upstream ->
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

  @JvmStatic
  fun <T> addUploadListener(
    url: String,
    progressListener: ProgressListener
  ): ObservableTransformer<T, T> = ObservableTransformer { upstream ->
    ProgressManager.getInstance().addRequestListener(url, progressListener)
    upstream
  }

  @JvmOverloads
  @JvmStatic
  fun <T> addDiffUploadListenerOnSameUrl(
    url: String,
    key: String? = null,
    progressListener: ProgressListener
  ): ObservableTransformer<T, T> = ObservableTransformer { upstream ->
    if (key == null) {
      ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, progressListener)
    } else {
      ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, key, progressListener)
    }
    upstream
  }

}