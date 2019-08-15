package com.dylanc.retrofit.helper.transformer

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
object ThreadTransformer {
  @JvmStatic
  fun <T> main(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      upstream.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    }
  }
}
