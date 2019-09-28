package com.dylanc.retrofit.helper.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @author Dylan Cai
 * @since 2019/9/26
 */
class GsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

  override fun responseBodyConverter(
    type: Type, annotations: Array<Annotation>,
    retrofit: Retrofit
  ): Converter<ResponseBody, *> {
    val adapter = gson.getAdapter(TypeToken.get(type))
    return GsonResponseBodyConverter(gson, adapter)
  }

  override fun requestBodyConverter(
    type: Type,
    parameterAnnotations: Array<Annotation>,
    methodAnnotations: Array<Annotation>,
    retrofit: Retrofit
  ): Converter<*, RequestBody> {
    val adapter = gson.getAdapter(TypeToken.get(type))
    return GsonRequestBodyConverter(gson, adapter)
  }

  companion object {
    /**
     * Create an instance using a default [Gson] instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    @JvmStatic
    fun create(): GsonConverterFactory {
      return create(Gson())
    }

    /**
     * Create an instance using `gson` for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    // Guarding public API nullability.
    @JvmStatic
    fun create(gson: Gson?): GsonConverterFactory {
      if (gson == null) throw NullPointerException("gson == null")
      return GsonConverterFactory(gson)
    }
  }
}
