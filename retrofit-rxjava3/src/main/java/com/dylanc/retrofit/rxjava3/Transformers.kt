@file:Suppress("unused")

package com.dylanc.retrofit.rxjava3

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

/**
 * @author Dylan Cai
 */

fun <T : Any> Observable<T>.io2mainThread(): Observable<T> =
  compose(Transformers.io2mainThread())

fun <T : Any> Flowable<T>.io2mainThread(): Flowable<T> =
  compose(Transformers.io2mainThread())

fun <T : Any> Single<T>.io2mainThread(): Single<T> =
  compose(Transformers.io2mainThread())

fun <T : Any> Observable<T>.showLoading(requestLoading: RequestLoading): Observable<T> =
  compose(Transformers.showLoading(requestLoading))

fun <T : Any> Flowable<T>.showLoading(requestLoading: RequestLoading): Flowable<T> =
  compose(Transformers.showLoading(requestLoading))

fun <T : Any> Single<T>.showLoading(requestLoading: RequestLoading): Single<T> =
  compose(Transformers.showLoading(requestLoading))

fun <T : Any> Observable<T>.showLoading(onLoading: (Boolean) -> Unit): Observable<T> =
  compose(Transformers.showLoading(onLoading))

fun <T : Any> Flowable<T>.showLoading(onLoading: (Boolean) -> Unit): Flowable<T> =
  compose(Transformers.showLoading(onLoading))

fun <T : Any> Single<T>.showLoading(onLoading: (Boolean) -> Unit): Single<T> =
  compose(Transformers.showLoading(onLoading))

fun <T : Any> Observable<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Observable<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

fun <T : Any> Flowable<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Flowable<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

fun <T : Any> Single<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Single<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

fun <T : Any> Observable<T>.showLoading(dialog: Dialog): Observable<T> =
  compose(Transformers.showLoading(dialog))

fun <T : Any> Flowable<T>.showLoading(dialog: Dialog): Flowable<T> =
  compose(Transformers.showLoading(dialog))

fun <T : Any> Single<T>.showLoading(dialog: Dialog): Single<T> =
  compose(Transformers.showLoading(dialog))

fun Observable<ResponseBody>.toFile(pathname: String): Observable<File> =
  compose(Transformers.toFile(pathname))

fun Flowable<ResponseBody>.toFile(pathname: String): Flowable<File> =
  compose(Transformers.toFile(pathname))

fun Single<ResponseBody>.toFile(pathname: String): Single<File> =
  compose(Transformers.toFile(pathname))

object Transformers {

  private const val TAG_LOADING = "loading"

  @JvmStatic
  fun <T : Any> io2mainThread() =
    ThreadTransformer<T>(Schedulers.io(), AndroidSchedulers.mainThread())

  @JvmStatic
  fun <T : Any> showLoading(requestLoading: RequestLoading) =
    LoadingTransformer<T>(requestLoading)

  @JvmStatic
  fun <T : Any> showLoading(onLoading: (Boolean) -> Unit) =
    LoadingTransformer<T> { isLoading -> onLoading(isLoading) }

  @JvmStatic
  @JvmOverloads
  fun <T : Any> showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment, tag: String = TAG_LOADING) =
    showLoading<T> { isLoading ->
      if (isLoading && !dialogFragment.isShowing) {
        dialogFragment.show(fragmentManager, tag)
      } else if (!isLoading && dialogFragment.isShowing) {
        dialogFragment.dismiss()
      }
    }

  @JvmStatic
  fun <T : Any> showLoading(dialog: Dialog) =
    showLoading<T> { isLoading ->
      if (isLoading && !dialog.isShowing) {
        dialog.show()
      } else if (!isLoading && dialog.isShowing) {
        dialog.dismiss()
      }
    }

  @JvmStatic
  fun toFile(pathname: String): FileTransformer =
    FileTransformer(pathname)

  private inline val DialogFragment.isShowing: Boolean
    get() = dialog?.isShowing == true && !isRemoving
}
