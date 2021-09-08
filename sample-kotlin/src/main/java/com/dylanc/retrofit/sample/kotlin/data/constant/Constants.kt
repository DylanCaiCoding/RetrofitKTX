@file:JvmName("Constants")

package com.dylanc.retrofit.sample.kotlin.data.constant

import com.dylanc.retrofit.annotations.BaseUrl
import com.dylanc.retrofit.annotations.DomainUrl

@BaseUrl
const val BASE_URL = "https://www.wanandroid.com"

//@DebugUrl
const val DEBUG_URL = "http://www.wanandroid.com"

@DomainUrl(name = "gank")
const val URL_GANK = "https://gank.io/api/"

const val DOWNLOAD_URL = "https://t7.baidu.com/it/u=3734967019,941734598&fm=193&f=GIF"
