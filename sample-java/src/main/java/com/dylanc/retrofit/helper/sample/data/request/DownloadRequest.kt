package com.dylanc.retrofit.helper.sample.data.request

import androidx.lifecycle.asLiveData
import com.dylanc.retrofit.helper.coroutines.ViewModelRequest
import com.dylanc.retrofit.helper.coroutines.livedata.catch
import com.dylanc.retrofit.helper.coroutines.livedata.showLoading
import com.dylanc.retrofit.helper.sample.data.repository.DataRepository

class DownloadRequest : ViewModelRequest() {

  fun download(url: String, path: String) =
    DataRepository.download(url, path)
      .showLoading(isLoading)
      .catch(exception)
      .asLiveData()
}