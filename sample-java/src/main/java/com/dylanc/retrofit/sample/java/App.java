package com.dylanc.retrofit.sample.java;

import android.app.Application;
import android.util.Log;

import com.dylanc.retrofit.cookiejar.PersistentCookieJarFactory;
import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.sample.java.constant.Constants;
import com.dylanc.retrofit.sample.java.network.GlobalErrorHandler;

import me.jessyan.progressmanager.ProgressManager;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    RetrofitHelper.getDefault()
        .baseUrl(Constants.BASE_URL)
        .retryOnConnectionFailure(false)
        .putDomain("gank", Constants.URL_GANK)
        .multipleDomains()
        .printHttpLog(BuildConfig.DEBUG)
        .doOnResponse(GlobalErrorHandler::handleResponse)
        .persistentCookiesJar(this)
        .cookieJar(PersistentCookieJarFactory.create(this))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .okHttpClientBuilder(builder -> {
          ProgressManager.getInstance().with(builder);
          ProgressManager.getInstance().setRefreshTime(10);
        })
        .init();
  }
}