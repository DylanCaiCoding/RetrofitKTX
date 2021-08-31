package com.dylanc.retrofit.coroutines

import androidx.fragment.app.DialogFragment

/**
 * @author Dylan Cai
 */
fun interface RequestDialogFactory {
  fun create(): DialogFragment
}
