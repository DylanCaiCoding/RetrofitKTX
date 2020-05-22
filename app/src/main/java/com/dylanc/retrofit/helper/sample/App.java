package com.dylanc.retrofit.helper.sample;

import android.app.Application;
import android.util.Log;

import com.dylanc.retrofit.helper.RetrofitHelper;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    RetrofitHelper.getDefault()
        .setDebug(true)
        .baseUrl("https://news.baidu.com/")
        .domain("gank", "http://gank.io/")
        .retryOnConnectionFailure(false) // 设置连接失败时重试
        .connectTimeout(15)
        .readTimeout(15)
        .writeTimeout(15)
        .addHttpLoggingInterceptor( message -> { // 打印日志
          Log.d("http",  message);
        })
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//        .addInterceptor(new HandleErrorInterceptor())
//        .addInterceptor(new HandleLoginInterceptor())
        .addDebugInterceptor(this, "user/login", R.raw.login_success) // 拦截请求返回本地的 json 文件内容
        .init();
  }
}