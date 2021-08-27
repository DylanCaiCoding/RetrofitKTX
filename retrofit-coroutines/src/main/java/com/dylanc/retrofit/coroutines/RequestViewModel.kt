package com.dylanc.retrofit.coroutines

import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.coroutines.livedata.ExceptionLiveData
import com.dylanc.retrofit.coroutines.livedata.LoadingLiveData

/**
 * @author Dylan Cai
 */
abstract class RequestViewModel : ViewModel() {
  val isLoading = LoadingLiveData()
  val exception = ExceptionLiveData()
}