package com.dylanc.retrofit.helper.coroutines

import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.helper.coroutines.livedata.ExceptionLiveData
import com.dylanc.retrofit.helper.coroutines.livedata.LoadingLiveData

/**
 * @author Dylan Cai
 */
abstract class RequestViewModel : ViewModel() {
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()
}