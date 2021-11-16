## 默认域名

用注解 `@BaseUrl` 配置默认域名，例如：

```java
public class Constants {
  @BaseUrl
  public static final String BASE_URL = "https://www.wanandroid.com";
}
```

## 测试域名

用注解 `@DebugUrl` 配置测试域名，并在初始化时配置了 debug 才会生效，不然会使用默认地址。例如：

```java
public class Constants {
  @DebugUrl
  public static final String DEBUG_URL = "http://192.168.1.3";
}
```

```java
RetrofitHelper.getDefault()
  .debug(BuildConfig.DEBUG)
   ...
  .init();
```

## 多域名

在接口类增加 `@ApiUrl` 注解来修改该类请求的 baseUrl，例如：

```java
@ApiUrl("https://gank.io")
public interface GankApi{
  @GET("/api/today")
  Single<String> getTodayList();
}
```

## 动态域名

在运行时请求的 baseUrl 会变，这个需求相对较少，通常只是不同域名，不需要运行时改变。所以本库将该功能移除了，但是代码仍有保留，有需要可以拷贝 [DomainsInterceptor](https://github.com/DylanCaiCoding/RetrofitHelper/blob/master/app/src/main/java/com/dylanc/retrofit/helper/sample/network/DomainsInterceptor.kt) 文件来使用。

接下来讲下用法，创建一个 map 来管理动态的地址，用代号+地址的方式保存，例如：

```java
public class Constants {
  public static HashMap<String, String> domains = new HashMap<>();

  static {
    domains.put("wanandroid", "https://www.wanandroid.com");
  }
}
```

初始化时添加 DomainsInterceptor，传入该 map，例如：

```java
RetrofitHelper.getDefault()
   ...
  .addInterceptor(new DomainsInterceptor(Constants.domains))
  .init();
```

在请求的方法增加一个 `DOMAIN_HEADER` + 代号的 header，例如：

```java
@Headers(Domain.DOMAIN_HEADER + "wanandroid")
@GET("/article/list/{page}/json")
Single<String> geArticleList(@Path(value = "page") int page);
```

上述的完成后，该请求的 baseUrl 就会使用代号所对应的地址，那么我们只需修改 map 的值，即可做到动态域名。例如：

```java
Constants.domains.put("wanandroid", "https://www.wanandroid2.com");
```
