package com.dylanc.retrofit.helper.transformer

import android.content.Context
import com.dylanc.retrofit.helper.RequestLoading
import com.dylanc.retrofit.helper.RetrofitHelper
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * @author Dylan Cai
 * @since 2019/8/15
 */
fun <T> Observable<T>.showLoading(
  context: Context,
  requestLoading: RequestLoading = RetrofitHelper.default.requestLoading!!
): Observable<T> = compose(LoadingTransformer.apply(context, requestLoading))

object LoadingTransformer {

  @JvmOverloads
  @JvmStatic
  fun <T> apply(
    context: Context,
    requestLoading: RequestLoading = RetrofitHelper.default.requestLoading!!
  ): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      upstream.doOnSubscribe {
        requestLoading.show(context)
      }.doOnTerminate {
        requestLoading.dismiss()
      }
    }
  }
}