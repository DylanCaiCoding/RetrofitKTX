@file:JvmName("Constants")

package com.dylanc.retrofit.helper.sample.constant

import com.dylanc.retrofit.helper.annotations.BaseUrl
import com.dylanc.retrofit.helper.annotations.DebugUrl
import com.dylanc.retrofit.helper.annotations.Domain

@BaseUrl
const val BASE_URL = "https://www.wanandroid.com"

@DebugUrl
const val DEBUG_URL = "http://www.wanandroid.com"

@Domain("gank")
const val URL_GANK = "https://gank.io"

const val DOWNLOAD_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563016090751&di=dd39865395e1bf58988a64fa3b077fe2&imgtype=0&src=http%3A%2F%2Fi1.hdslb.com%2Fbfs%2Farchive%2Fa724c9dedb0cb263c64a1f69571e92c9f2999a87.jpg"
