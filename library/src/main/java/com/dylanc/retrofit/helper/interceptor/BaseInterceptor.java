package com.dylanc.retrofit.helper.interceptor;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

import java.util.LinkedHashMap;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class BaseInterceptor implements Interceptor {

  protected String getUrlParam(Chain chain, String key) {
    return chain.request().url().queryParameter(key);
  }

  protected LinkedHashMap<String, String> getUrlParams(Chain chain) {
    final HttpUrl url = chain.request().url();
    final LinkedHashMap<String, String> params = new LinkedHashMap<>();
    for (int i = 0; i < url.querySize(); i++) {
      params.put(url.queryParameterName(i), url.queryParameterValue(i));
    }
    return params;
  }

  protected String getBodyParam(Chain chain, String key) {
    return getBodyParams(chain).get(key);
  }

  protected LinkedHashMap<String, String> getBodyParams(Chain chain) {
    final FormBody body = (FormBody) chain.request().body();
    final LinkedHashMap<String, String> params = new LinkedHashMap<>();
    if (body != null) {
      for (int i = 0; i < body.size(); i++) {
        params.put(body.name(i), body.value(i));
      }
    }
    return params;
  }
}
