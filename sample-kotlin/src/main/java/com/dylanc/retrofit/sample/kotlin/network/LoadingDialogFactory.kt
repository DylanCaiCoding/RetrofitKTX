package com.dylanc.retrofit.sample.kotlin.network

import androidx.fragment.app.DialogFragment
import com.dylanc.retrofit.coroutines.RequestDialogFactory
import com.dylanc.retrofit.sample.kotlin.widget.LoadingDialogFragment

/**
 * @author Dylan Cai
 */
class LoadingDialogFactory : RequestDialogFactory {

  override fun invoke(): DialogFragment {
    return LoadingDialogFragment()
  }
}