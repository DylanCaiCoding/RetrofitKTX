# 开始上手

## 添加依赖

在根目录的 build.gradle 添加：

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://www.jitpack.io' }
    }
}
```

添加依赖：

```groovy
dependencies {
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-ktx:1.0.0-beta'
    kapt 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-compiler:1.0.0-beta'

    // 可选
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-coroutines:1.0.0-beta'
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-rxjava2:1.0.0-beta'
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-rxjava3:1.0.0-beta'
}
```

## 初始化

初始化是非必要的，以下是可选的常用配置：

```kotlin
initRetrofit(BuildConfig.DEBUG) {
  okHttpClient {
    addHeaders("name1" to "value1", "name2" to "value2") // 全局 headers
    printHttpLog { Log.i("http", it) } // 打印日志
    cacheControl {
      if (!isNetworkAvailable) {
        maxAge(1, TimeUnit.DAYS) // 比如在网络不可用时取一天内的缓存
      } 
    }
    multipleDomains() // 支持动态域名
    persistentCookies() // 持久化 cookies
    connectTimeout(15, TimeUnit.SECONDS)
    writeTimeout(15, TimeUnit.SECONDS)
    readTimeout(15, TimeUnit.SECONDS)
    retryOnConnectionFailure(false)
    authenticator(authenticator)
    addInterceptor(interceptor)
  }
  addConverterFactory(ScalarsConverterFactory.create())
  addCallAdapterFactory(RxJava3CallAdapterFactory.create())
}
```