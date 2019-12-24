package com.dylanc.retrofit.helper.transformer

import android.content.Context
import com.dylanc.retrofit.helper.RequestLoading
import com.dylanc.retrofit.helper.RetrofitHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.progressmanager.body.ProgressInfo

/**
 * @author Dylan Cai
 * @since 2019/12/17
 */
object Transformers {

  @JvmStatic
  fun <T> io2mainThread() =
    ThreadTransformer<T>(Schedulers.io(), AndroidSchedulers.mainThread())

  @JvmStatic
  fun <T> showLoading(
    context: Context,
    requestLoading: RequestLoading = RetrofitHelper.getDefault().requestLoading!!
  ) = LoadingTransformer<T>(context, {
    requestLoading.show(context)
  }, {
    requestLoading.dismiss()
  })

  @JvmStatic
  fun <T> observeDownload(
    url: String,
    onProgress: (progressInfo: ProgressInfo?) -> Unit,
    onError: (id: Long, e: Exception?) -> Unit
  ) = ProgressTransformer<T>(onProgress, onError) { listener ->
    ProgressManager.getInstance().addResponseListener(url, listener)
  }

  @JvmStatic
  fun <T> observeDiffDownloadOnSameUrl(
    url: String,
    key: String? = null,
    onProgress: (progressInfo: ProgressInfo?) -> Unit,
    onError: (id: Long, e: Exception?) -> Unit
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
    onProgress: (progressInfo: ProgressInfo?) -> Unit,
    onError: (id: Long, e: Exception?) -> Unit
  ) = ProgressTransformer<T>(onProgress, onError) { listener ->
    ProgressManager.getInstance().addRequestListener(url, listener)
  }

  @JvmStatic
  fun <T> observeDiffUploadOnSameUrl(
    url: String,
    key: String? = null,
    onProgress: (progressInfo: ProgressInfo?) -> Unit,
    onError: (id: Long, e: Exception?) -> Unit
  ) = ProgressTransformer<T>(onProgress, onError) { listener ->
    if (key == null) {
      ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, listener)
    } else {
      ProgressManager.getInstance().addDiffRequestListenerOnSameUrl(url, key, listener)
    }
  }

}