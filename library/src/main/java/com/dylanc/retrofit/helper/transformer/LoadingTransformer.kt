package com.dylanc.retrofit.helper.transformer

import android.app.Dialog
import android.content.Context
import com.dylanc.retrofit.helper.ILoadingDialog
import com.dylanc.retrofit.helper.RetrofitHelper
import io.reactivex.ObservableTransformer

/**
 * @author Dylan Cai
 * @since 2019/8/15
 */
object LoadingTransformer {
  private lateinit var dialog: Dialog

  @JvmStatic
  fun <T> apply(context: Context): ObservableTransformer<T, T> {
    return apply(context,RetrofitHelper.default.loadingDialog!!)
  }

  @JvmStatic
  fun <T> apply(context: Context, loadingDialog: ILoadingDialog): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
      upstream.doOnSubscribe {
        dialog = loadingDialog.onCreate(context)
        dialog.show()
      }.doOnTerminate {
        dialog.dismiss()
      }
    }
  }
}