# 处理后台的骚数据

这是个人写的一篇文章[《优雅地处理后台返回的骚数据》](https://juejin.im/post/6844903975028785159)所提供的方案，或者说该文章是我封装此功能时有感而发。简单总结就是，后台返回的数据不方便我们解析，建议让后台人员改。如果实现没办法沟通后仍不肯改，就自己手动预处理数据，改成自己想要的结构，这样解析就不会报错。

在初始化时可以配置 doOnRepsonse 方法预处理数据，例如：

```java
RetrofitHelper.getDefault()
  .doOnResponse((response, url, body) -> {
    ...
  })
  .init();
```

具体的预处理代码怎么写呢，可以参考文章里的示例。