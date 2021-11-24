# 发起请求

## 准备工作

添加相应的 RxJava 依赖：

```
dependencies {
  implementation 'com.dylanc:retrofit-helper-rxjava:1.2.0'
}
```

初始化时配置 `RxJava2CallAdapterFactory`：

```java
RetrofitHelper.getDefault()
  .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  .init();
```

如果要在请求的时候显示加载弹框，先创建一个类实现 RequestLoading 的接口，重写 show 法。例如：
```java
class LoadingDialog extends DialogFragment implements RequestLoading {

  private FragmentActivity fragmentActivity;

  public LoadingDialog(FragmentActivity fragmentActivity) {
    this.fragmentActivity = fragmentActivity;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    return new AlertDialog.Builder(fragmentActivity)
        .setTitle("loading")
        .setMessage("wait a minute...")
        .setCancelable(false)
        .create();
  }

  @Override
  public void show(boolean isLoading) {
    if (isLoading) {
      show(fragmentActivity.getSupportFragmentManager(), "loading");
    } else {
      dismiss();
    }
  }
}

```

## Get 请求

```java
public interface ArticleApi {
  @GET("/article/list/{page}/json")
  Single<String> geArticleList(@Path(value = "page") int page);
}
```

```java
RetrofitHelper.create(ArticleApi.class)
  .geArticleList(page)
  .compose(Transformers.io2mainThread())                       // 切换线程
  .compose(Transformers.showLoading(new LoadingDialog(this)))  // 显示弹框，参数是 RequestLoading 接口
  .as(AutoDisposable.bind(this))                               // 绑定生命周期
  .subscribe(
      json -> {
        Toast.makeText(this, json, Toast.LENGTH_SHORT).show()
      },
      e -> {
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show();
      }
  );
```

## Post 请求

```java
public interface UserApi{
  @FormUrlEncoded
  @POST("/user/login")
  Single<String> login(@Field("username") String username, @Field("password") String password);
}
```

```java
RetrofitHelper.create(UserApi.class)
  .login(username, password)
  .compose(Transformers.io2mainThread())
  .compose(Transformers.showLoading(new RxLoadingDialog(this)))
  .as(AutoDisposable.bind(this))
  .subscribe(
      json -> {
        Toast.makeText(this, json, Toast.LENGTH_SHORT).show()
      },
      e -> {
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show();
      }
  );
```

## 上传文件

```java
public interface UploadApi{
  @Multipart
  @POST("/file/upload")
  Single<String> uploadImage(@Part MultipartBody.Part image); // 单文件上传

  @Multipart
  @POST("/file/upload")
  Single<String> uploadImages(@Part List<MultipartBody.Part> images); // 多文件上传
}
```

```java
RetrofitHelper.create(UploadApi.class)
  .uploadImage(PartFactory.create(path, "file"))
  //.uploadImages(PartFactory.create(pathList, "files"))
  .compose(Transformers.io2mainThread())
  .compose(Transformers.showLoading(new RxLoadingDialog(this)))
  .as(AutoDisposable.bind(this))
  .subscribe(
      json -> {
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
      },
      e -> {
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show();
      }
  );
```

## 下载文件

```java
public interface UploadApi{
  @Streaming
  @GET
  Single<String> download(@Url String url); 
}
```

```java
RetrofitHelper.create(DownloadApi.class)
  .download(url)
  .compose(Transformers.toFile(pathname))
  .compose(Transformers.io2mainThread())
  .compose(Transformers.showLoading(new RxLoadingDialog(this)))
  .as(AutoDisposable.bind(this))
  .subscribe(
      file -> {
        Toast.makeText(this, "已下载到" + file.getPath(), Toast.LENGTH_SHORT).show()
      },
      e -> {
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show();
      }
  );
```