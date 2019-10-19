package com.dylanc.retrofit.helper.sample;

import android.app.Application;
import android.util.Log;

import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.helper.sample.network.HandleErrorInterceptor;
import com.dylanc.retrofit.helper.sample.network.RequestLoadingDialog;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    RetrofitHelper.getDefault()
        .setDebug(true)
        .baseUrl("https://www.baidu.com/")
        .debugUrl("https://news.baidu.com/")
        .putDomain("gank", "http://gank.io/")
        .retryOnConnectionFailure(false) // 设置连接失败时重试
        .connectTimeout(15)
        .readTimeout(15)
        .writeTimeout(15)
        .progressRefreshTime(100) // 设置上传下载进度的刷新时间间隔
        .requestLoading(new RequestLoadingDialog()) // 设置默认的 loading 样式
        .setPersistentCookieJar(this) // 持久化保存 cookie
        .addHttpLoggingInterceptor( message -> { // 打印日志
          Log.d("http",  message);
        })
        .addInterceptor(new HandleErrorInterceptor())
        .addDebugInterceptor(this, "user/login", R.raw.login_failure) // 拦截请求返回本地的 json 文件内容
        .init();
  }
}