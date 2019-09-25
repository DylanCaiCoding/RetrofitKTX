package com.dylanc.retrofit.helper.transformer

import android.content.Context
import com.dylanc.retrofit.helper.IRequestLoading
import com.dylanc.retrofit.helper.RetrofitHelper
import io.reactivex.ObservableTransformer

/**
 * @author Dylan Cai
 * @since 2019/8/15
 */
object LoadingTransformer {

  @JvmStatic
  fun <T> apply(context: Context): ObservableTransformer<T, T> {
    return apply(context,RetrofitHelper.default.requestLoading!!)
  }

  @JvmStatic
  fun <T> apply(context: Context, requestLoading: IRequestLoading): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      upstream.doOnSubscribe {
        requestLoading.show(context)
      }.doOnTerminate {
        requestLoading.dismiss()
      }
    }
  }
}