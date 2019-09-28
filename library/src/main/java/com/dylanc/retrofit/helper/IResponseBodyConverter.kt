package com.dylanc.retrofit.helper

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody

/**
 * @author Dylan Cai
 * @since 2019/9/28
 */
interface IResponseBodyConverter {
  fun <T> convert(value: ResponseBody, gson: Gson, adapter: TypeAdapter<T>): T
}