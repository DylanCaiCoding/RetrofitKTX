package com.dylanc.retrofit.helper.transformer

import android.content.Context
import com.dylanc.retrofit.helper.RequestLoading
import com.dylanc.retrofit.helper.RetrofitHelper
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
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
fun <T> Flowable<T>.io2mainThread(): Flowable<T> =
  compose(FlowableTransformers.io2mainThread())

fun <T> Flowable<T>.showLoading(
  context: Context,
  requestLoading: RequestLoading = RetrofitHelper.getDefault().requestLoading!!
): Flowable<T> = compose(FlowableTransformers.showLoading(context, requestLoading))

fun Flowable<ResponseBody>.toFile(pathname: String): Flowable<File> =
  compose(FlowableTransformers.toFile(pathname))

fun Flowable<ResponseBody>.addDownloadListener(
  url: String,
  progressListener: ProgressListener
): Flowable<ResponseBody> =
  compose(FlowableTransformers.addDownloadListener(url, progressListener))

fun Flowable<ResponseBody>.addDiffDownloadListenerOnSameUrl(
  url: String,
  key: String? = null,
  progressListener: ProgressListener
): Flowable<ResponseBody> =
  compose(FlowableTransformers.addDiffDownloadListenerOnSameUrl(url, key, progressListener))

fun <T> Flowable<T>.addUploadListener(
  url: String,
  progressListener: ProgressListener
): Flowable<T> =
  compose(FlowableTransformers.addUploadListener(url, progressListener))

fun <T> Flowable<T>.addDiffUploadListenerOnSameUrl(
  url: String,
  key: String? = null,
  progressListener: ProgressListener
): Flowable<T> =
  compose(FlowableTransformers.addDiffUploadListenerOnSameUrl(url, key, progressListener))


object FlowableTransformers {

  @JvmStatic
  fun <T> io2mainThread(): FlowableTransformer<T, T> = FlowableTransformer { upstream ->
    upstream.subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  @JvmOverloads
  @JvmStatic
  fun <T> showLoading(
    context: Context,
    requestLoading: RequestLoading = RetrofitHelper.getDefault().requestLoading!!
  ): FlowableTransformer<T, T> = FlowableTransformer { upstream ->
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
  ): FlowableTransformer<ResponseBody, ResponseBody> = FlowableTransformer { upstream ->
    ProgressManager.getInstance().addResponseListener(url, progressListener)
    upstream
  }

  @JvmOverloads
  @JvmStatic
  fun addDiffDownloadListenerOnSameUrl(
    url: String,
    key: String? = null,
    progressListener: ProgressListener
  ): FlowableTransformer<ResponseBody, ResponseBody> = FlowableTransformer { upstream ->
    if (key == null) {
      ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, progressListener)
    } else {
      ProgressManager.getInstance().addDiffResponseListenerOnSameUrl(url, key, progressListener)
    }
    upstream
  }

  @JvmStatic
  fun toFile(pathname: String): FlowableTransformer<ResponseBody, File> =
    FlowableTransformer { upstream ->
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
  ): FlowableTransformer<T, T> = FlowableTransformer { upstream ->
    ProgressManager.getInstance().addRequestListener(url, progressListener)
    upstream
  }

  @JvmOverloads
  @JvmStatic
  fun <T> addDiffUploadListenerOnSameUrl(
    url: String,
    key: String? = null,
    progressListener: ProgressListener
  ): FlowableTransformer<T, T> = FlowableTransformer { upstream ->
    if (key == null) {
      ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, progressListener)
    } else {
      ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, key, progressListener)
    }
    upstream
  }

}