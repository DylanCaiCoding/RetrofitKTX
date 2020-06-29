@file:Suppress("unused")

package com.dylanc.retrofit.helper.rxjava

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 */

fun <T> Observable<T>.io2mainThread(): Observable<T> =
  compose(Transformers.io2mainThread())

fun <T> Flowable<T>.io2mainThread(): Flowable<T> =
  compose(Transformers.io2mainThread())

fun <T> Single<T>.io2mainThread(): Single<T> =
  compose(Transformers.io2mainThread())

fun <T> Observable<T>.showLoading(requestLoading: RequestLoading): Observable<T> =
  compose(Transformers.showLoading(requestLoading))

fun <T> Flowable<T>.showLoading(requestLoading: RequestLoading): Flowable<T> =
  compose(Transformers.showLoading(requestLoading))

fun <T> Single<T>.showLoading(requestLoading: RequestLoading): Single<T> =
  compose(Transformers.showLoading(requestLoading))

fun <T> Observable<T>.showLoading(onLoading: (Boolean) -> Unit): Observable<T> =
  compose(Transformers.showLoading(onLoading))

fun <T> Flowable<T>.showLoading(onLoading: (Boolean) -> Unit): Flowable<T> =
  compose(Transformers.showLoading(onLoading))

fun <T> Single<T>.showLoading(onLoading: (Boolean) -> Unit): Single<T> =
  compose(Transformers.showLoading(onLoading))

fun Observable<ResponseBody>.toFile(pathname: String): Observable<File> =
  compose(Transformers.toFile(pathname))

fun Flowable<ResponseBody>.toFile(pathname: String): Flowable<File> =
  compose(Transformers.toFile(pathname))

fun Single<ResponseBody>.toFile(pathname: String): Single<File> =
  compose(Transformers.toFile(pathname))

object Transformers {

  @JvmStatic
  fun <T> io2mainThread() =
    ThreadTransformer<T>(Schedulers.io(), AndroidSchedulers.mainThread())

  @JvmStatic
  fun <T> showLoading(requestLoading: RequestLoading) =
    LoadingTransformer<T>(requestLoading)

  @JvmStatic
  fun <T> showLoading(onLoading: (Boolean) -> Unit) =
    LoadingTransformer<T>(object : RequestLoading {
      override fun show(isShow: Boolean) {
        onLoading(isShow)
      }
    })

  @JvmStatic
  fun toFile(pathname: String): FileTransformer =
    FileTransformer(pathname)
}