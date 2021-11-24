# 持久化 cookie

初始化时配置持久化 cookie，例如：

```java
RetrofitHelper.getDefault()
  .cookieJar(PersistentCookies.create(this))
  .init();
```

如果退出的时候想清理 cookie ，可以调用清理方法，例如：

```java
PersistentCookies.clear()
```