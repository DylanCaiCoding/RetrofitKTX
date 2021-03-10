package com.dylanc.retrofit.helper.sample;

import android.app.Application;
import android.util.Log;

import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.helper.cookie.PersistentCookie;
import com.dylanc.retrofit.helper.sample.network.DebugInterceptor;
import com.dylanc.retrofit.helper.sample.network.GlobalErrorHandler;

import java.io.File;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;
import me.jessyan.progressmanager.ProgressManager;
import okhttp3.CacheControl;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    RetrofitHelper.getDefault()
        .debug(BuildConfig.DEBUG)
        .retryOnConnectionFailure(false)
        .cache(new File(getCacheDir(), "response"), 10 * 1024 * 1024,
            (url) -> new CacheControl.Builder().maxAge(1, TimeUnit.DAYS).build())
        .addHttpLog(message -> {
          Log.i("http", message);
        })
        .doOnResponse(GlobalErrorHandler::handleResponse)
        .cookieJar(PersistentCookie.create(this))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addInterceptor(new DebugInterceptor(this, "user/login", R.raw.login_success), true)
        .okHttpClientBuilder(builder -> {
          ProgressManager.getInstance().with(builder);
          ProgressManager.getInstance().setRefreshTime(10);
          return Unit.INSTANCE;
        })
        .init();
  }
}