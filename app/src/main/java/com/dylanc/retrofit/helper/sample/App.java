package com.dylanc.retrofit.helper.sample;

import android.app.Application;
import android.util.Log;
import com.dylanc.retrofit.helper.RetrofitHelper;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

public class App extends Application {
  private static final String TAG = "App";

  @Override
  public void onCreate() {
    super.onCreate();
    RetrofitHelper.getDefault()
        .setDebugMode(true)
        .setBaseUrl("https://news.baidu.com/")
        .putDomain("gank", "http://gank.io/")
        .addLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
          @Override
          public void log(@NotNull String s) {
            Log.d(TAG, "log: " + s);
          }
        })
        .setCookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this)))
//        .addDebugInterceptor(this, "guonei", R.raw.user)
        .init();
  }
}
