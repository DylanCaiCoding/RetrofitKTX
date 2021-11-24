# 管理 headers

## 添加全局 header

初始化时调用 `addHeaders()` 配置全局 headers。

```kotlin
initRetrofit {
  okHttpClient {
    addHeaders("name1" to "value1", "name2" to "value2")
  }
}
```

## 添加静态 header

在接口方法添加 `@Headers` 注解进行修饰。

```kotlin
interface UserApi {
  @Headers("Content-Type: application/json;charset=UTF-8")
  @GET("/user")
  suspend fun geUser(): User
}
```

## 动态添加 header

在接口方法添加 `@Header` 注解修饰的参数，在发起请求时才传入 header。

```kotlin
interface UserApi {
  @GET("/user")
  suspend fun geUser(@Header("Country-Code") countryCode: String): User
}
```

如果有很多接口方法需要动态添加相同的 header，建议使用下面的方式进行配置，支持对 url 进行判断。

```kotlin
initRetrofit {
  // ...
  addHeaders { request ->
    if (request.url.toString().startsWith("...")) {
      put("Country-Code", countryCode)
    }
  }
}
```