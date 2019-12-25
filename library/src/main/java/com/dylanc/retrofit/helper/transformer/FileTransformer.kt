package com.dylanc.retrofit.helper.transformer

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.reactivestreams.Publisher
import java.io.File
import java.io.InputStream

/**
 * @author Dylan Cai
 * @since 2019/12/17
 */
fun InputStream.toFile(pathname: String) = File(pathname).apply {
  use { input ->
    outputStream().use { fileOut ->
      input.copyTo(fileOut)
    }
  }
}

class FileTransformer(private val pathname: String) : ObservableTransformer<ResponseBody, File>,
  FlowableTransformer<ResponseBody, File>, SingleTransformer<ResponseBody, File>,
  MaybeTransformer<ResponseBody, File> {
  override fun apply(upstream: Observable<ResponseBody>): ObservableSource<File> =
    upstream.subscribeOn(Schedulers.io())
      .unsubscribeOn(Schedulers.io())
      .map { responseBody -> responseBody.byteStream() }
      .observeOn(Schedulers.computation())
      .map { inputStream -> inputStream.toFile(pathname) }
      .observeOn(AndroidSchedulers.mainThread())

  override fun apply(upstream: Flowable<ResponseBody>): Publisher<File> =
    upstream.subscribeOn(Schedulers.io())
      .unsubscribeOn(Schedulers.io())
      .map { responseBody -> responseBody.byteStream() }
      .observeOn(Schedulers.computation())
      .map { inputStream -> inputStream.toFile(pathname) }
      .observeOn(AndroidSchedulers.mainThread())

  override fun apply(upstream: Single<ResponseBody>): SingleSource<File> =
    upstream.subscribeOn(Schedulers.io())
      .unsubscribeOn(Schedulers.io())
      .map { responseBody -> responseBody.byteStream() }
      .observeOn(Schedulers.computation())
      .map { inputStream -> inputStream.toFile(pathname) }
      .observeOn(AndroidSchedulers.mainThread())

  override fun apply(upstream: Maybe<ResponseBody>): MaybeSource<File> =
    upstream.subscribeOn(Schedulers.io())
      .unsubscribeOn(Schedulers.io())
      .map { responseBody -> responseBody.byteStream() }
      .observeOn(Schedulers.computation())
      .map { inputStream -> inputStream.toFile(pathname) }
      .observeOn(AndroidSchedulers.mainThread())
}