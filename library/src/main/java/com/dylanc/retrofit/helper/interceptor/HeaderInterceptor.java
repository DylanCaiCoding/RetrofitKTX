package com.dylanc.retrofit.helper.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dylan Cai
 * @since 2019/7/11
 */
@SuppressWarnings("unused")
public class HeaderInterceptor implements Interceptor {
  private HashMap<String,String> mHeaders = new HashMap<>();

  public HeaderInterceptor() {
  }

  public HeaderInterceptor(HashMap<String, String> headers) {
    mHeaders = headers;
  }

  public void addHeader(String name, String value){
    mHeaders.put(name,value);
  }

  @NotNull
  @Override
  public Response intercept(@NotNull Chain chain) throws IOException {
    Request request = chain.request();
    Request.Builder builder = request.newBuilder()
        .method(request.method(),request.body());
    for (Map.Entry<String, String> entry : mHeaders.entrySet()) {
      builder.header(entry.getKey(),entry.getValue());
    }
    return chain.proceed(builder.build());
  }
}
