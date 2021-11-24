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