@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.rxjava

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
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

inline fun <T> Observable<T>.io2mainThread(): Observable<T> =
  compose(Transformers.io2mainThread())

inline fun <T> Flowable<T>.io2mainThread(): Flowable<T> =
  compose(Transformers.io2mainThread())

inline fun <T> Single<T>.io2mainThread(): Single<T> =
  compose(Transformers.io2mainThread())

inline fun <T> Observable<T>.showLoading(requestLoading: RequestLoading): Observable<T> =
  compose(Transformers.showLoading(requestLoading))

inline fun <T> Flowable<T>.showLoading(requestLoading: RequestLoading): Flowable<T> =
  compose(Transformers.showLoading(requestLoading))

inline fun <T> Single<T>.showLoading(requestLoading: RequestLoading): Single<T> =
  compose(Transformers.showLoading(requestLoading))

inline fun <T> Observable<T>.showLoading(noinline onLoading: (Boolean) -> Unit): Observable<T> =
  compose(Transformers.showLoading(onLoading))

inline fun <T> Flowable<T>.showLoading(noinline onLoading: (Boolean) -> Unit): Flowable<T> =
  compose(Transformers.showLoading(onLoading))

inline fun <T> Single<T>.showLoading(noinline onLoading: (Boolean) -> Unit): Single<T> =
  compose(Transformers.showLoading(onLoading))

inline fun <T> Observable<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Observable<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

inline fun <T> Flowable<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Flowable<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

inline fun <T> Single<T>.showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment): Single<T> =
  compose(Transformers.showLoading(fragmentManager, dialogFragment))

inline fun <T> Observable<T>.showLoading(dialog: Dialog): Observable<T> =
  compose(Transformers.showLoading(dialog))

inline fun <T> Flowable<T>.showLoading(dialog: Dialog): Flowable<T> =
  compose(Transformers.showLoading(dialog))

inline fun <T> Single<T>.showLoading(dialog: Dialog): Single<T> =
  compose(Transformers.showLoading(dialog))

inline fun Observable<ResponseBody>.toFile(pathname: String): Observable<File> =
  compose(Transformers.toFile(pathname))

inline fun Flowable<ResponseBody>.toFile(pathname: String): Flowable<File> =
  compose(Transformers.toFile(pathname))

inline fun Single<ResponseBody>.toFile(pathname: String): Single<File> =
  compose(Transformers.toFile(pathname))

object Transformers {

  private const val TAG_LOADING = "loading"

  @JvmStatic
  fun <T> io2mainThread() =
    ThreadTransformer<T>(Schedulers.io(), AndroidSchedulers.mainThread())

  @JvmStatic
  fun <T> showLoading(requestLoading: RequestLoading) =
    LoadingTransformer<T>(requestLoading)

  @JvmStatic
  fun <T> showLoading(onLoading: (Boolean) -> Unit) =
    LoadingTransformer<T> { isLoading -> onLoading(isLoading) }

  @JvmStatic
  @JvmOverloads
  fun <T> showLoading(fragmentManager: FragmentManager, dialogFragment: DialogFragment, tag: String = TAG_LOADING) =
    showLoading<T> { isLoading ->
      if (isLoading && !dialogFragment.isShowing) {
        dialogFragment.show(fragmentManager, tag)
      } else if (!isLoading && dialogFragment.isShowing) {
        dialogFragment.dismiss()
      }
    }

  @JvmStatic
  fun <T> showLoading(dialog: Dialog) =
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
