# RetrofitHelper

[![Download](https://api.bintray.com/packages/dylancai/maven/retrofit-helper-core/images/download.svg)](https://bintray.com/dylancai/maven/retrofit-helper-core/_latestVersion)  [![License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://github.com/DylanCaiCoding/RetrofitHelper/blob/master/LICENSE)

## 简介

RetrofitHelper 是使用 Kotlin 封装的 Retrofit 工具，让 Retrofit 的请求更加简单方便。

- 兼顾 Java 和 Kotlin 的使用。结合了 Kotlin 的特性，在使用 Kotlin 请求时会更加简洁；
- 用注解管理 baseUrl；
- 增加 debug 模式；
- 可配置 loading 弹框；
- 对协程进行了封装优化；

## 用法

Kotlin | [Java](https://github.com/DylanCaiCoding/RetrofitHelper/blob/master/README_JAVA.md)

在 `build.gradle` 添加依赖:

```gradle
dependencies {
  implementation 'com.dylanc:retrofit-helper-core:1.2.0'
  kapt 'com.dylanc:retrofit-helper-compiler:1.2.0'
  // 可选
  implementation 'com.dylanc:retrofit-helper-rxjava:1.2.0'
  implementation 'com.dylanc:retrofit-helper-autodispose:1.2.0'
  implementation 'com.dylanc:retrofit-helper-coroutines:1.2.0'
}
```

### 初始化

初始化是非必要的，以下是可选的常用配置：

```kotlin
initRetrofit {
  debug(BuildConfig.DEBUG)
  addHeader("key", "value")
  cache(File(cacheDir, "response"), 10 * 1024 * 1024) { // 缓存策略
    if (!NetworkUtils.isAvailable()) {
      cacheControl { maxAge(1, TimeUnit.DAYS) } // 比如在网络不可用时取一天内的缓存
    } else {
      null
    }
  }
  addHttpLog { Log.d("http", it) }  // 开启了 debug 模式才会打印日志
  connectTimeout(15)
  writeTimeout(15)
  readTimeout(15)
  retryOnConnectionFailure(false)
  authenticator(authenticator)
  cookieJar(cookieJar)
  addInterceptor(interceptor)
  addNetworkInterceptor(networkInterceptor)
  addConverterFactory(FastJsonConverterFactory.create())
  addCallAdapterFactory(RxJava2CallAdapterFactory.create())
}
```

如果上述提供的常用配置方法还不满足需求，可以直接对 OkHttpClientBuilder 和 RetrofitBuilder 进行配置，比如：

```kotlin
initRetrofit {
  ...
  okHttpClientBuilder {
    sslSocketFactory(sslSocketFactory)
  }
  retrofitBuilder {
    validateEagerly(false)
  }
}
```

### 配置 baseUrl

用注解 `@BaseUrl` 配置 baseUrl。如果初始化时设置了 debug ，会使用 `@DebugUrl` 注解修饰的域名。

```kotlin
@BaseUrl
const val BASE_URL = "https://www.wanandroid.com/"

@DebugUrl  // 可选
const val DEBUG_URL = "http://192.168.1.3"
```

如果想实现多 baseUrl，在接口类增加 `@ApiUrl` 注解来修改该类请求的 baseUrl。

```kotlin
@ApiUrl("https://gank.io")
interface GankApi{
  @GET("/api/today")
  fun getTodayList(): Single<String>
}
```

### 网络请求

#### 使用 RxJava

添加 rxjava 和 autodispose 依赖，并配置 `RxJava2CallAdapterFactory`：

```kotlin
initRetrofit {
  addCallAdapterFactory(RxJava2CallAdapterFactory.create())
}
```

如果需要在请求的时候显示 loading 动画，先实现 `RequestLoading` 接口：

```kotlin
class LoadingDialog(private val fragmentActivity: FragmentActivity) : DialogFragment(), RequestLoading {
  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return AlertDialog.Builder(fragmentActivity)
        .setTitle("loading")
        .setMessage("wait a minute...")
        .setCancelable(false)
        .create()
  }

  override fun show(isLoading: Boolean) {
    if (isLoading) {
      show(fragmentActivity.supportFragmentManager, "loading")
    } else {
      dismiss()
    }
  }
}
```

ps：下面的例子只是展示基础的用法，实际的开发建议根据需要使用 `compose` 操作符封装减少调用的方法数。

##### Get 请求

```kotlin
interface ArticleApi {
  @GET("/article/list/{page}/json")
  fun geArticleList(@Path(value = "page") page:Int): Single<String>
}
```

```kotlin
apiServiceOf<ArticleApi>()
  .geArticleList(page)
  .io2mainThread()                  // 切换线程
  .showLoading(LoadingDialog(this)) // 显示弹框，参数是 RequestLoading 接口
  .autoDispose(this)                // 绑定生命周期
  .subscribe({ json ->
    Toast.makeText(this, json, Toast.LENGTH_SHORT).show()
  }, { e ->
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  })
```

##### Post 请求

```kotlin
interface UserApi{
  @FormUrlEncoded
  @POST("/user/login")
  fun login(@Field("username") username: String, @Field("password") password: String): Single<String>
}
```

```kotlin
apiServiceOf<UserApi>()
  .login(username, password)
  .io2mainThread()
  .showLoading(LoadingDialog(this))
  .autoDispose(this)
  .subscribe({ json ->
    Toast.makeText(this, json, Toast.LENGTH_SHORT).show()
  }, { e ->
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  })
```

##### 上传文件

```kotlin
interface UploadApi{
  @Multipart
  @POST("/file/upload")
  fun uploadImage(@Part image: MultipartBody.Part): Single<String> // 单文件上传

  @Multipart
  @POST("/file/upload")
  fun uploadImages(@Part images: List<MultipartBody.Part>): Single<String> // 多文件上传
}
```

```kotlin
apiServiceOf<UploadApi>()
  .uploadImage(path.toPart("file"))
  //.uploadImages(pathList.toPartList("files"))
  .io2mainThread()
  .showLoading(LoadingDialog(this))
  .autoDispose(this)
  .subscribe({
    Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
  }, { e ->
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  })
```

##### 下载文件

```kotlin
interface DownloadApi{
  @Streaming
  @GET
  fun download(@Url url: String): Single<ResponseBody>
}
```

```kotlin
apiServiceOf<DownloadApi>()
  .download(url)
  .toFile(pathname)
  .showLoading(LoadingDialog(this))
  .autoDispose(this)
  .subscribe({ file ->
    Toast.makeText(this, "已下载到${file.path}", Toast.LENGTH_SHORT).show()
  }, { e ->
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  })
```

#### 使用协程

待补充。

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
