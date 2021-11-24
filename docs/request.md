# 发起请求

## 协程回调

## RxJava 回调

### 准备工作

添加相应的 RxJava 依赖：

```groovy
dependencies {
    // implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-rxjava2:1.0.0-beta'
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-rxjava3:1.0.0-beta'
}
```

初始化时配置 `RxJava2CallAdapterFactory`：

```kotlin
initRetrofit {
  // addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  addCallAdapterFactory(RxJava3CallAdapterFactory.create())
}
```

如果要在请求的时候显示加载弹框，先创建一个类实现 RequestLoading 的接口，重写 show 法。例如：
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

### Get 请求

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

### Post 请求

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

### 上传文件

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

### 下载文件

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