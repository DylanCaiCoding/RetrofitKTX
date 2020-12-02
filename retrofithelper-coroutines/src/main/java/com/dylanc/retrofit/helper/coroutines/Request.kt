@file:Suppress("unused")

package com.dylanc.retrofit.helper.coroutines

abstract class Request(
  val isLoading: LoadingLiveData,
  val exception: ExceptionLiveData
)