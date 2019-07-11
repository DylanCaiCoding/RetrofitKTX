package com.dylanc.retrofit.helper.interceptor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import okhttp3.*;

import java.io.*;

public class DebugInterceptor implements Interceptor {
  private final Context mContext;
  private final String mDebugUrl;
  private final int mDebugRawId;

  public DebugInterceptor(Context context, String debugUrl, int debugRawId) {
    mContext = context;
    mDebugUrl = debugUrl;
    mDebugRawId = debugRawId;
  }

  @NonNull
  @Override
  public Response intercept(@NonNull Chain chain) throws IOException {
    final String url = chain.request().url().toString();
    if (url.contains(mDebugUrl)) {
      return debugResponse(chain, mDebugRawId);
    }
    return chain.proceed(chain.request());
  }

  private Response debugResponse(Chain chain, @RawRes int rawId) {
    final String json = getRawFile(mContext, rawId);
    return createResponse(chain, json);
  }

  private Response createResponse(Chain chain, String json) {
    return new Response.Builder()
        .code(200)
        .addHeader("Content-Type", "application/json")
        .body(ResponseBody.create(MediaType.parse("application/json"), json))
        .message("OK")
        .request(chain.request())
        .protocol(Protocol.HTTP_1_1)
        .build();
  }

  private static String getRawFile(Context context, int id) {
    final InputStream is = context.getResources().openRawResource(id);
    final BufferedInputStream bis = new BufferedInputStream(is);
    final InputStreamReader isr = new InputStreamReader(bis);
    final BufferedReader br = new BufferedReader(isr);
    final StringBuilder stringBuilder = new StringBuilder();
    String str;
    try {
      while ((str = br.readLine()) != null) {
        stringBuilder.append(str);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
        isr.close();
        bis.close();
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return stringBuilder.toString();
  }
}
