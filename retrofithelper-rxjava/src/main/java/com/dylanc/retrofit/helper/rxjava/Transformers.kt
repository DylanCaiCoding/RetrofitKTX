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
 * @since 2019/12/17
 */
fun <T> Observable<T>.io2mainThread(): Observable<T> =
  compose(Transformers.io2mainThread())

fun <T> Observable<T>.showLoading(
  requestLoading: RequestLoading
): Observable<T> = compose(Transformers.showLoading(requestLoading))

fun Observable<ResponseBody>.toFile(pathname: String): Observable<File> =
  compose(Transformers.toFile(pathname))

fun <T> Flowable<T>.io2mainThread(): Flowable<T> =
  compose(Transformers.io2mainThread())

fun <T> Flowable<T>.showLoading(
  requestLoading: RequestLoading
): Flowable<T> = compose(Transformers.showLoading(requestLoading))

fun Flowable<ResponseBody>.toFile(pathname: String): Flowable<File> =
  compose(Transformers.toFile(pathname))

fun <T> Single<T>.io2mainThread(): Single<T> =
  compose(Transformers.io2mainThread())

fun <T> Single<T>.showLoading(
  requestLoading: RequestLoading
): Single<T> = compose(Transformers.showLoading(requestLoading))

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
  fun toFile(pathname: String): FileTransformer =
    FileTransformer(pathname)
}