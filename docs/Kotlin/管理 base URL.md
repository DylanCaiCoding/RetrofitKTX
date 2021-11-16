## 默认域名

用注解 `@BaseUrl` 配置默认域名，例如：

```kotlin
@BaseUrl
const val BASE_URL = "https://www.wanandroid.com/"
```

## 测试域名

用注解 `@DebugUrl` 配置测试域名，并在初始化时配置了 debug 才会生效，不然会使用默认地址。例如：

```kotlin
@DebugUrl
const val DEBUG_URL = "http://192.168.1.3"
```

```kotlin
initRetrofit {
  debug(BuildConfig.DEBUG)
  ...
}
```

## 静态多域名

在接口类增加 `@ApiUrl` 注解来修改该类所有请求方法的 baseUrl，例如：

```kotlin
@ApiUrl("https://gank.io")
interface GankApi{
  @GET("/api/today")
  fun getTodayList(): Single<String>
}
```

## 动态域名

在请求的方法增加 `DomainName`注解，为该请求标注一个域名代号，例如：

```kotlin
@DomainName("wanandroid")
@GET("/article/list/{page}/json")
fun geArticleList(@Path(value = "page") page: Int): Single<String>
```

在一个地址常量增加 `@DomainUrl` 注解并传入上面的域名代号，可修改该域名代号的默认地址，例如：

```kotlin
@DomainUrl("wanandroid")
const val WAN_ANDROID_URL = "https://www.wanandroid.com"
```

在运行时想动态修改域名，只需修改 retrofitDomains 中对应代号的值即可。例如：

```kotlin
retrofitDomains["wanandroid"] = "https://www.wanandroid.com/v2"
```
