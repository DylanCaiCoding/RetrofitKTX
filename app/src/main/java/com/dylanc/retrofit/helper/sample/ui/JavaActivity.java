package com.dylanc.retrofit.helper.sample.ui;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.helper.rxjava.DownloadApi;
import com.dylanc.retrofit.helper.rxjava.Transformers;
import com.dylanc.retrofit.helper.sample.R;
import com.dylanc.retrofit.helper.sample.api.TestApi;
import com.dylanc.retrofit.helper.sample.constant.Constants;
import com.dylanc.retrofit.helper.sample.network.RxLoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.Objects;

public class JavaActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void requestArticleList(View view) {
    RetrofitHelper.create(TestApi.class)
        .geArticleList(0)
        .compose(Transformers.io2mainThread())
        .compose(Transformers.showLoading(new RxLoadingDialog(this)))
        .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(
            this::toast,
            e -> toast(e.getMessage())
        );
  }

  public void requestGankTodayList(View view) {
    RetrofitHelper.create(TestApi.class)
        .getGankTodayList()
        .compose(Transformers.io2mainThread())
        .compose(Transformers.showLoading(new RxLoadingDialog(this)))
        .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(
            this::toast,
            e -> toast(e.getMessage())
        );
  }

  public void requestLogin(View view) {
    RetrofitHelper.create(TestApi.class)
        .login()
        .compose(Transformers.io2mainThread())
        .compose(Transformers.showLoading(new RxLoadingDialog(this)))
        .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(
            response -> toast("登录成功"),
            e -> toast(e.getMessage())
        );
  }

  public void download(View view) {
    final String pathname = Objects.requireNonNull(getExternalCacheDir()).getPath() + "/test.png";
    new RxPermissions(this)
        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(aBoolean -> {
          RetrofitHelper.create(DownloadApi.class)
              .download(Constants.DOWNLOAD_URL)
              .compose(Transformers.toFile(pathname))
              .compose(Transformers.io2mainThread())
              .compose(Transformers.showLoading(new RxLoadingDialog(this)))
              .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
              .subscribe(
                  file -> toast("已下载到" + file.getPath()),
                  e -> toast(e.getMessage())
              );
        });
  }

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }
}
