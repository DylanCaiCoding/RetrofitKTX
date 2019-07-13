package com.dylanc.retrofit.helper.transformer

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
class DownloadTransformer {
  companion object {
    @JvmStatic
    fun handle(pathname: String): ObservableTransformer<ResponseBody, File> {
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
}