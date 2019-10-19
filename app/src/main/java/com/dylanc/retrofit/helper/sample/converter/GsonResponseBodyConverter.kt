package com.dylanc.retrofit.helper.sample.converter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 * @author Dylan Cai
 * @since 2019/9/26
 */
class GsonResponseBodyConverter<T>(
  private val gson: Gson,
  private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {

  @Throws(IOException::class)
  override fun convert(value: ResponseBody): T {
    val jsonReader = gson.newJsonReader(value.charStream())
    value.use {
      val result = adapter.read(jsonReader)
      if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
        throw JsonIOException("JSON document was not fully consumed.")
      }
      return result
    }
  }
}
