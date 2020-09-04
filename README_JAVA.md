# RetrofitHelper

[![Download](https://api.bintray.com/packages/dylancai/maven/retrofit-helper/images/download.svg)](https://bintray.com/dylancai/maven/retrofit-helper/_latestVersion) [![License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://github.com/DylanCaiCoding/RetrofitHelper/blob/master/LICENSE)


## 用法

[Kotlin](https://github.com/DylanCaiCoding/RetrofitHelper) | Java

在 `build.gradle` 添加依赖:

```gradle
dependencies {
  implementation 'com.dylanc:retrofit-helper:1.2.0-beta2'
  kapt 'com.dylanc:retrofit-helper-compiler:1.2.0-beta2'
}
```

### 初始化

使用 `@BaseUrl` 注解配置 BaseUrl，例如：

```java
public class Constants {
  @BaseUrl
  public static final String BASE_URL = "https://www.wanandroid.com";
}
```

没有其它配置要求就可以直接请求了，如有需要可以进行以下配置：

```java
RetrofitHelper.getDefault()
  .addHeader("key", "value")
  .cache(new File(getCacheDir(), "response"), 10 * 1024 * 1024,
      () -> new CacheControl.Builder().maxAge(10, TimeUnit.MINUTES).build())
  .connectTimeout(15)
  .writeTimeout(15)
  .readTimeout(15)
  .retryOnConnectionFailure(false)
  .authenticator(authenticator)
  .cookieJar(cookieJar)
  .addInterceptor(interceptor)
  .addNetworkInterceptor(networkInterceptor)
  .addConverterFactory(FastJsonConverterFactory.create())
  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  .init();
```

如果上述提供的常用配置方法还不满足需求，可以配置 OkHttpClientBuilder 和 RetrofitBuilder 达到所需的效果，比如：

```kotlin
RetrofitHelper.getDefault()
  // 其它配置
  .okHttpClientBuilder(builder -> {
    builder.sslSocketFactory(sslSocketFactory);
    return Unit.INSTANCE;
  })
  .retrofitBuilder(builder -> {
    builder.validateEagerly(false);
    return Unit.INSTANCE;
  })
  .init();
```

### 网络请求

#### 使用 RxJava

添加相应的依赖和配置 `RxJava2CallAdapterFactory`：

```gradle
dependencies {
  implementation 'com.dylanc:retrofit-helper-rxjava:1.1.1'
}
```

```java
RetrofitHelper.getDefault()
  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  .init();
```

如果需要在请求的时候显示 loading 动画，先实现 `RequestLoading` 接口：

```java
public class RxLoadingDialog implements RequestLoading {
  private LoadingDialogFragment loadingDialog = new LoadingDialogFragment();
  private FragmentActivity activity;

  public RxLoadingDialog(FragmentActivity activity) {
    this.activity = activity;
  }

  @Override
  public void show() {
    loadingDialog.show(activity.getSupportFragmentManager(), "loading");
  }

  @Override
  public void dismiss() {
    loadingDialog.dismiss();
  }

  public static class LoadingDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
      if (getActivity() == null) {
        throw new IllegalStateException("Activity cannot be null");
      }
      return new AlertDialog.Builder(getActivity())
          .setTitle("loading")
          .setMessage("wait a minute...")
          .setCancelable(false)
          .create();
    }
  }
}
```

ps：下面的例子只是展示基础的用法，实际的开发建议根据需要使用 `compose` 操作符封装减少调用的方法数。

##### Get 请求

```java
public interface ArticleApi {
  @GET("/article/list/{page}/json")
  Single<String> geArticleList(@Path(value = "page") int page);
}
```

```java
RetrofitHelper.create(ArticleApi.class)
  .geArticleList(page)
  .compose(Transformers.io2mainThread())
  .compose(Transformers.showLoading(new RxLoadingDialog(this)))
  .as(AutoDisposable.bind(this))
  .subscribe(
    json -> Toast.makeText(this, json, Toast.LENGTH_SHORT).show(),
    e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  );
```

##### Post 请求

键值对的请求方式大同小异，这里以传 Json 数据的请求方式为例：

```java
public interface UserApi{
  @POST("/user/login")
  Single<String> login(@Body RequestBody requestBody);
}
```

```java
Map<String, Object> params = new HashMap<>();
params.put("username", username);
params.put("password", password);
RetrofitHelper.create(UserApi.class)
  .login(RequestBodyFactory.create(params))
  .compose(Transformers.io2mainThread())
  .compose(Transformers.showLoading(new RxLoadingDialog(this)))
  .as(AutoDisposable.bind(this))
  .subscribe(
    json -> Toast.makeText(this, json, Toast.LENGTH_SHORT).show(),
    e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  );
```

##### 上传文件

```java
public interface UploadApi{
  @Multipart
  @POST("/file/upload")
  Single<String> uploadImage(@Part MultipartBody.Part image); // 单文件上传

  @Multipart
  @POST("/file/upload")
  Single<String> uploadImages(@Part List<MultipartBody.Part> images); // 多文件上传
}
```

```java
RetrofitHelper.create(UploadApi.class)
  .uploadImage(PartFactory.create(path, "file"))
  //.uploadImages(PartFactory.create(pathList, "files"))
  .compose(Transformers.io2mainThread())
  .compose(Transformers.showLoading(new RxLoadingDialog(this)))
  .as(AutoDisposable.bind(this))
  .subscribe(
    json -> Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show(),
    e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  );
```

##### 下载文件

```java
public interface UploadApi{
  @Streaming
  @GET
  Single<String> download(@Url String url); 
}
```

```java
RetrofitHelper.create(DownloadApi.class)
  .download(url)
  .compose(Transformers.toFile(pathname))
  .compose(Transformers.io2mainThread())
  .compose(Transformers.showLoading(new RxLoadingDialog(this)))
  .as(AutoDisposable.bind(this))
  .subscribe(
    file -> Toast.makeText(this, "已下载到" + file.getPath(), Toast.LENGTH_SHORT).show(),
    e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  );
```

### 其他用法

#### 调试模式

初始化时配置 debug，以下的功能才会生效。

```java
RetrofitHelper.getDefault()
  .debug(BuildConfig.DEBUG)
  .init();
```

##### 支持测试环境地址

通常测试环境和生产环境的地址不一样，打不同的包经常改来改去会很麻烦，所以提供了 `@DebugUrl` 进行配置。如果没有使用该注解，会获取 `@BaseUrl` 配置的地址。

```java
public class Constants {
  @BaseUrl
  public static final String BASE_URL = "https://www.wanandroid.com";
  @DebugUrl
  public static final String DEBUG_URL = "http://192.168.1.3";
}
```

##### 打印请求数据日志

请求的日志不应该在正式环境打印出来，所以限制了在 debug 模式下才会执行回调。

```java
RetrofitHelper.getDefault()
  .debug(BuildConfig.DEBUG)
  .addHttpLog(message -> {
    Log.i(TAG, message);
  })
  .init();
```

#### 运行时动态修改 BaseUrl

在 url 常量增加 `@Domain` 注解：

```java
public class Constants {
  @Domain("gank")
  public static final String URL_GANK = "https://gank.io";
}
```

请求的方法增加 Header：

```java
public interface GankApi{
  @Headers(DOMAIN_HEADER + "gank")
  @GET("/api/today")
  Single<String> getTodayList();
}
```

如果在运行时需要动态修改域名：

```java
RetrofitHelper.putDomain("gank", "https://gank2.io");
```

### 混淆

如果开启了混淆，增加以下配置：

```
-keep class com.dylanc.retrofit.helper.*{*;}
```

## License

```
Copyright (C) 2019. Dylan Cai

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
