package com.dylanc.retrofit.helper.sample.constant

import com.dylanc.retrofit.helper.annotations.BaseUrl
import com.dylanc.retrofit.helper.annotations.DebugUrl
import com.dylanc.retrofit.helper.annotations.Domain

@BaseUrl
const val BASE_URL = "http://www.baidu.com"

@DebugUrl
const val DEBUG_URL = "http://news.baidu.com"

@Domain("gank")
const val URL_GANK = "http://gank.io"