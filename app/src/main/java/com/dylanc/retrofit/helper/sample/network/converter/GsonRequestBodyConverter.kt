package com.dylanc.retrofit.helper.sample.network.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.IOException
import java.io.OutputStreamWriter
import java.nio.charset.Charset

/**
 * @author Dylan Cai
 * @since 2019/9/26
 */
class GsonRequestBodyConverter<T>(
  private val gson: Gson,
  private val adapter: TypeAdapter<T>
) :
  Converter<T, RequestBody> {

  @Throws(IOException::class)
  override fun convert(value: T): RequestBody {
    val buffer = Buffer()
    val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
    val jsonWriter = gson.newJsonWriter(writer)
    adapter.write(jsonWriter, value)
    jsonWriter.close()
    return buffer.readByteString().toRequestBody(MEDIA_TYPE)
  }

  companion object {
    private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaType()
    private val UTF_8 = Charset.forName("UTF-8")
  }
}
