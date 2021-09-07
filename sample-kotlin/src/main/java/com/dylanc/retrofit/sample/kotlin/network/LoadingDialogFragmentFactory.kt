package com.dylanc.retrofit.sample.kotlin.network

import androidx.fragment.app.DialogFragment
import com.dylanc.retrofit.coroutines.loading.RequestDialogFragmentFactory
import com.dylanc.retrofit.sample.kotlin.widget.LoadingDialogFragment

/**
 * @author Dylan Cai
 */
class LoadingDialogFragmentFactory : RequestDialogFragmentFactory() {

  override fun create(): DialogFragment {
    return LoadingDialogFragment()
  }
}