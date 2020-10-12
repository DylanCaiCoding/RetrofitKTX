package com.dylanc.retrofit.helper.sample;

import android.app.Application;
import android.util.Log;

import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.helper.sample.network.DebugInterceptor;
import com.dylanc.retrofit.helper.sample.network.HandleErrorInterceptor;
import com.dylanc.retrofit.helper.sample.network.PersistentCookie;

import kotlin.Unit;
import me.jessyan.progressmanager.ProgressManager;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    RetrofitHelper.getDefault()
        .debug(BuildConfig.DEBUG)
        .retryOnConnectionFailure(false)
//        .cache(new File(getCacheDir(), "response"), 10 * 1024 * 1024,
//            (url) -> new CacheControl.Builder().maxAge(1, TimeUnit.DAYS).build())
        .addHttpLog(message -> {
          Log.i("http", message);
        })
        .cookieJar(PersistentCookie.create(this))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addInterceptor(new HandleErrorInterceptor())
        .addInterceptor(new DebugInterceptor(this, "user/login", R.raw.login_success), true)
        .okHttpClientBuilder(builder -> {
          ProgressManager.getInstance().with(builder);
          ProgressManager.getInstance().setRefreshTime(10);
          return Unit.INSTANCE;
        })
        .init();
  }
}