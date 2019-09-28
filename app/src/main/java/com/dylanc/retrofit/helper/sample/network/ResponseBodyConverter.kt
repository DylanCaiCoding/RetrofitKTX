package com.dylanc.retrofit.helper.sample.network

import com.dylanc.retrofit.helper.IResponseBodyConverter
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import kotlin.text.Charsets.UTF_8

/**
 * @author Dylan Cai
 * @since 2019/9/28
 */
class ResponseBodyConverter:IResponseBodyConverter {
  override fun <T> convert(value: ResponseBody, gson: Gson, adapter: TypeAdapter<T>): T {
        val json = value.string()
    val jsonObject = JSONObject(json)
    if (jsonObject.optInt("code", -1) != 200) {
      throw RuntimeException(jsonObject.getString("msg"))
    }
    val contentType = value.contentType()
    val charset = if (contentType != null) contentType.charset(UTF_8) else UTF_8
    val inputStream = ByteArrayInputStream(json.toByteArray())
    val reader = InputStreamReader(inputStream, charset!!)
    val jsonReader = gson.newJsonReader(reader)
    value.use {
      val result = adapter.read(jsonReader)
      if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
        throw JsonIOException("JSON document was not fully consumed.")
      }
      return result
    }
  }
}