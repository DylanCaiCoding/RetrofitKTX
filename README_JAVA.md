# RetrofitHelper

[![Download](https://api.bintray.com/packages/dylancai/maven/retrofit-helper-core/images/download.svg)](https://bintray.com/dylancai/maven/retrofit-helper-core/_latestVersion) [![License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://github.com/DylanCaiCoding/RetrofitHelper/blob/master/LICENSE)

## 简介

RetrofitHelper 是使用 Kotlin 封装的 Retrofit 工具，让 Retrofit 的请求更加简单方便。

- 兼顾 Java 和 Kotlin 的使用。结合了 Kotlin 的特性，在使用 Kotlin 请求时会更加简洁；
- 用注解管理 baseUrl；
- 增加 debug 模式；
- 可配置 loading 弹框；
- 对协程进行了封装优化；

## 用法

[Kotlin](https://github.com/DylanCaiCoding/RetrofitHelper) | Java

在 `build.gradle` 添加依赖:

```gradle
dependencies {
  implementation 'com.dylanc:retrofit-helper-core:1.2.0'
  annotationProcessor 'com.dylanc:retrofit-helper-compiler:1.2.0'
  // 可选
  implementation 'com.dylanc:retrofit-helper-rxjava:1.2.0'
  implementation 'com.dylanc:retrofit-helper-autodispose:1.2.0'
}
```

### 初始化

初始化是非必要的，以下是可选的常用配置：

```java
RetrofitHelper.getDefault()
  .debug(BuildConfig.DEBUG)
  .addHeader("key", "value")
  .cache(new File(getCacheDir(), "response"), 10 * 1024 * 1024, // 缓存策略
      () -> {
        if (!NetworkUtils.isAvailable()) {
          return new CacheControl.Builder().maxAge(10, TimeUnit.MINUTES).build()); // 比如在网络不可用时取一天内的缓存
        } else {
          return null;
        }
      }
  .addHttpLog(message -> { // 开启了 debug 模式才会打印日志
    Log.i("http", message);
  })
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

如果上述提供的常用配置方法还不满足需求，可以直接对 OkHttpClientBuilder 和 RetrofitBuilder 进行配置，比如：

```java
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

### 配置 baseUrl

用注解 `@BaseUrl` 配置 baseUrl。如果初始化时设置了 debug ，会使用 `@DebugUrl` 注解修饰的域名。

```java
public class Constants {
  @BaseUrl
  public static final String BASE_URL = "https://www.wanandroid.com";
    
  @DebugUrl //可选
  public static final String DEBUG_URL = "http://192.168.1.3";
}
```

如果想实现多 baseUrl，在接口类增加 `@ApiUrl` 注解来修改该类请求的 baseUrl。

```java
@ApiUrl("https://gank.io")
public interface GankApi{
  @GET("/api/today")
  Single<String> getTodayList();
}
```

### 网络请求

#### 使用 RxJava

添加 rxjava 和 autodispose 依赖，并配置 `RxJava2CallAdapterFactory`：

```java
RetrofitHelper.getDefault()
  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  .init();
```

如果需要在请求的时候显示 loading 动画，先实现 `RequestLoading` 接口：

```java
class LoadingDialog extends DialogFragment implements RequestLoading {

  private FragmentActivity fragmentActivity;

  public LoadingDialog(FragmentActivity fragmentActivity) {
    this.fragmentActivity = fragmentActivity;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    return new AlertDialog.Builder(fragmentActivity)
        .setTitle("loading")
        .setMessage("wait a minute...")
        .setCancelable(false)
        .create();
  }

  @Override
  public void show(boolean isLoading) {
    if (isLoading) {
      show(fragmentActivity.getSupportFragmentManager(), "loading");
    } else {
      dismiss();
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

```java
public interface UserApi{
  @FormUrlEncoded
  @POST("/user/login")
  Single<String> login(@Field("username") String username, @Field("password") String password);
}
```

```java
RetrofitHelper.create(UserApi.class)
  .login(username, password)
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
