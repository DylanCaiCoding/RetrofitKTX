package com.dylanc.retrofit.helper.sample;

import android.app.Application;
import android.util.Log;
import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.helper.RetrofitManager;
import com.dylanc.retrofit.helper.sample.network.ResponseBodyConverter;
import com.dylanc.retrofit.helper.sample.network.RequestLoadingDialog;

public class App extends Application {

  private static final String TAG = "App";

  @Override
  public void onCreate() {
    super.onCreate();
    RetrofitHelper.getDefault()
        .debugMode(true)
        .baseUrl("https://www.baidu.com/")
        .debugUrl("https://news.baidu.com/")
        .putDomain("gank", "http://gank.io/")
        .progressRefreshTime(10) // 设置上传下载进度的刷新时间间隔
        .requestLoading(new RequestLoadingDialog()) // 设置默认的 loading 样式
        .addLoggingInterceptor(s -> Log.d(TAG, "log: " + s)) // 输出日志
        .addDebugInterceptor(this, "user/login", R.raw.login_success) // 拦截请求返回本地的 json 文件内容
        .addResponseBodyConverter(new ResponseBodyConverter()) // 自定义 Gson 解析请求响应结果
        .setPersistentCookieJar(this) // 持久化保存 cookie
        .init();
  }
}
