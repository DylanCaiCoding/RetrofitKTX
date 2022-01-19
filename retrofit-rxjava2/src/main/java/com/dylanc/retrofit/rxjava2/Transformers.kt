@file:Suppress("unused")

package com.dylanc.retrofit.rxjava2

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.dylanc.retrofit.rxjava2.RequestLoading.Companion.TAG_LOADING
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

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

fun <T> Observable<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Observable<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

fun <T> Flowable<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Flowable<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

fun <T> Single<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Single<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

fun <T> Observable<T>.showLoading(dialog: Dialog): Observable<T> =
  compose(Transformers.showLoading(dialog))

fun <T> Flowable<T>.showLoading(dialog: Dialog): Flowable<T> =
  compose(Transformers.showLoading(dialog))

fun <T> Single<T>.showLoading(dialog: Dialog): Single<T> =
  compose(Transformers.showLoading(dialog))

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
  @JvmOverloads
  fun <T> showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment, tag: String = TAG_LOADING) =
    showLoading<T>(RequestLoading.create(fragmentManager, dialogFragment, tag))

  @JvmStatic
  fun <T> showLoading(dialog: Dialog) =
    showLoading<T>(RequestLoading.create(dialog))

  @JvmStatic
  fun toFile(pathname: String): FileTransformer =
    FileTransformer(pathname)
}
