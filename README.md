# RetrofitHelper

[English]() | 中文

[![Download](https://api.bintray.com/packages/dylancai/maven/retrofit-helper/images/download.svg)](https://bintray.com/dylancai/maven/retrofit-helper/_latestVersion)[![License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://github.com/DylanCaiCoding/RetrofitHelper/blob/master/LICENSE)


## 用法

在 `build.gradle` 添加依赖:

```gradle
dependencies {
  implementation 'com.dylanc:retrofit-helper:1.1.0'
  kapt 'com.dylanc:retrofit-helper-compiler:1.1.0'
}
```

### 初始化

使用注解配置 BaseUrl，例如：

```kotlin
@BaseUrl
const val BASE_URL = "https://www.wanandroid.com"
```

没有其它配置要求就可以直接请求了，如有需要可以进行以下配置：

```kotlin
initRetrofit {
  addHeader("key", "value")
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

如果上述提供的常用配置方法还不满足需求，可以配置 OkHttpClientBuilder 和 RetrofitBuilder 达到所需的效果，比如：

```kotlin
initRetrofit {
  // 其它配置
  okHttpClientBuilder {
    sslSocketFactory(sslSocketFactory)
  }
  retrofitBuilder {
    validateEagerly(false)
  }
}
```

#### 调试模式

初始化时配置 debug，下面的功能才会生效。

```kotlin
initRetrofit {
  debug(BuildConfig.DEBUG)
}
```

##### 支持测试环境地址

通常测试环境和生产环境的地址不一样，打不同的包经常改来改去会很麻烦，所以提供了 `@DebugUrl` 进行配置。如果没有使用该注解，会获取 `@BaseUrl` 配置的地址。

```kotlin
@DebugUrl
const val DEBUG_URL = "http://192.168.1.3"
```

##### 打印请求数据日志

请求的日志不应该在正式环境打印出来，所以限制了在 debug 模式下才会执行回调。

```kotlin
initRetrofit {
  debug(BuildConfig.DEBUG)
  addHttpLoggingInterceptor{ msg ->
    Log.i(TAG,  msg);
  }
}
```

### 网络请求

#### 使用 RxJava

添加相应的依赖和配置 `RxJava2CallAdapterFactory `：

```gradle
dependencies {
  implementation 'com.dylanc:retrofit-helper-rxjava:1.1.0'
}
```

```kotlin
initRetrofit {
  addCallAdapterFactory(RxJava2CallAdapterFactory.create())
}
```

如果需要在请求的时候显示 loading 动画，先实现 `RequestLoading` 接口：

```kotlin
class RxLoadingDialog(private val activity: FragmentActivity) : RequestLoading {

  private val loadingDialog = LoadingDialogFragment()

  override fun show() {
    loadingDialog.show(activity.supportFragmentManager, "loading")
  }

  override fun dismiss() {
    loadingDialog.dismiss()
  }

  class LoadingDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      return activity?.let {
        AlertDialog.Builder(it)
          .setTitle("loading")
          .setMessage("wait a minute...")
          .setCancelable(false)
          .create()
      } ?: throw IllegalStateException("Activity cannot be null")
    }
  }
}
```

ps：下面的例子只是展示基础的用法，实际的开发建议根据需要使用 `compose` 操作符封装减少调用的方法数。

##### Get 请求

```kotlin
interface ArticleApi {
  @GET("/article/list/{page}/json")
  fun geArticleList(
    @Path(value = "page") page:Int
  ): Single<String>
}
```

```kotlin
apiServiceOf<ArticleApi>()
  .geArticleList(page)
  .io2mainThread()
  .showLoading(RxLoadingDialog(this))
  .autoDispose(this)
  .subscribe({
    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
  }, { e ->
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  })
```

##### Post 请求

键值对的请求方式大同小异，这里以传 Json 数据的请求方式为例：

```kotlin
interface UserApi{
  @POST("/user/login")
  fun login(requestBody: RequestBody): Single<String>
}
```

```kotlin
apiServiceOf<UserApi>()
  .login(jsonBodyOf(
    "username" to username,
    "password" to password
  ))
  .io2mainThread()
  .showLoading(RxLoadingDialog(this))
  .autoDispose(this)
  .subscribe({
    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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
  fun uploadImages(@Part images: List<MultipartBody.Part>): Single<String> //多文件上传
}
```

```kotlin
apiServiceOf<UploadApi>()
  .uploadImage(path.toPart("file"))
  //.uploadImages(pathList.toPartList("files"))
  .io2mainThread()
  .showLoading(RxLoadingDialog(this))
  .autoDispose(this)
  .subscribe({
    Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
  }, { e ->
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  })
```

##### 下载文件

```kotlin
apiServiceOf<DownloadApi>()
  .download(url)
  .toFile(pathname)
  .io2mainThread()
  .showLoading(RxLoadingDialog(this))
  .autoDispose(this)
  .subscribe({ file ->
    Toast.makeText(this, "已下载到${file.path}", Toast.LENGTH_SHORT).show()
  }, { e ->
    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
  })
```

#### 使用协程

可以结合协程进行异步处理，不过暂未针对协程进行代码的封装优化，后续对协程封装后再补充例子。

### 其他用法

#### 运行时动态修改 BaseUrl

在 url 常量增加 `@Domain` 注解：

```kotlin
@Domain("gank")
const val URL_GANK = "https://gank.io"
```

请求的方法增加 Header：

```kotlin
interface GankApi{
  @Headers(DOMAIN_HEADER + "gank")
  @GET("/api/today")
  fun getTodayList(): Single<String>
}
```

如果在运行时需要动态修改域名：

```kotlin
putDomain("gank", "https://gank2.io")
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