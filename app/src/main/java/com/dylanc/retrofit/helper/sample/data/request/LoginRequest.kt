package com.dylanc.retrofit.helper.sample.data.request

import com.dylanc.retrofit.helper.coroutines.ExceptionLiveData
import com.dylanc.retrofit.helper.coroutines.LoadingLiveData
import com.dylanc.retrofit.helper.coroutines.Request

class LoginRequest(isLoading: LoadingLiveData, exception: ExceptionLiveData) :
  Request(isLoading, exception) {
}