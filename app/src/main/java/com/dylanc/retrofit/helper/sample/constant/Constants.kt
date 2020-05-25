package com.dylanc.retrofit.helper.sample.constant

import com.dylanc.retrofit.helper.annotation.DefaultDomain
import com.dylanc.retrofit.helper.annotation.Domain

@DefaultDomain
const val BASE_URL = "http://www.baidu.com"
@Domain(DOMAIN_GANK)
const val URL_GANK = "http://gank.io"

const val DOMAIN_GANK = "gank"