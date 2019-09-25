package com.dylanc.retrofit.helper.sample;

import android.app.Application;
import android.util.Log;
import com.dylanc.retrofit.helper.RetrofitHelper;

public class App extends Application {
  private static final String TAG = "App";

  @Override
  public void onCreate() {
    super.onCreate();
    RetrofitHelper.getDefault()
        .debugMode(true)
        .baseUrl("https://news.baidu.com/")
        .downloadRefreshTime(1)
        .putDomain("gank", "http://gank.io/")
        .defaultRequestLoading(new RequestLoadingDialog())
        .addLoggingInterceptor(s -> Log.d(TAG, "log: " + s))
        .addDebugInterceptor(this, "login", R.raw.user)
        .setPersistentCookieJar(this)
        .init();
  }
}
