package com.dylanc.retrofit.helper.sample;

import android.app.Application;
import android.util.Log;
import com.dylanc.retrofit.helper.PersistentCookie;
import com.dylanc.retrofit.helper.RetrofitHelper;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

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
        .addLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
          @Override
          public void log(@NotNull String s) {
            Log.d(TAG, "log: " + s);
          }
        })
        .addDebugInterceptor(this, "login", R.raw.user)
        .cookieJar(new PersistentCookie(this))
        .init();
  }
}
