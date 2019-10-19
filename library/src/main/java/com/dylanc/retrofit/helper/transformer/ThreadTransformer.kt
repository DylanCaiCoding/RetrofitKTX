package com.dylanc.retrofit.helper.transformer

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.progressmanager.ProgressListener
import okhttp3.ResponseBody

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */

fun <T> Observable<T>.io2mainThread(): Observable<T> = compose(ThreadTransformer.io2main())

object ThreadTransformer {
  @JvmStatic
  fun <T> io2main(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      upstream.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    }
  }
}
