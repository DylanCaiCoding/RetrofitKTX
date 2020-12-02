package com.dylanc.retrofit.helper.sample.ui

import androidx.lifecycle.ViewModel
import com.dylanc.retrofit.helper.coroutines.requests
import com.dylanc.retrofit.helper.sample.data.request.DownloadRequest
import com.dylanc.retrofit.helper.sample.data.request.LoginRequest
import com.dylanc.retrofit.helper.sample.data.request.TestRequest

class KotlinCoroutinesViewModel : ViewModel() {

  val testRequest: TestRequest by requests()
  val loginRequest: LoginRequest by requests()
  val downloadRequest: DownloadRequest by requests()
  val allRequest = listOf(testRequest, loginRequest, downloadRequest)

}