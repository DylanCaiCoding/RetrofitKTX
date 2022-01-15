package com.dylanc.retrofit.sample.java.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.rxjava2.RxDownloadApi;
import com.dylanc.retrofit.rxjava2.Transformers;
import com.dylanc.retrofit.sample.java.R;
import com.dylanc.retrofit.sample.java.api.GankApi;
import com.dylanc.retrofit.sample.java.api.RxJavaApi;
import com.dylanc.retrofit.sample.java.constant.Constants;
import com.dylanc.retrofit.sample.java.network.LoadingDialog;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.Objects;

public class JavaActivity extends AppCompatActivity {

  private final LoadingDialog loadingDialog = new LoadingDialog();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
  }

  public void requestArticleList(View view) {
    RetrofitHelper.create(RxJavaApi.class)
        .geArticleList(0)
        .compose(Transformers.io2mainThread())
        .compose(Transformers.showLoading(getSupportFragmentManager(), loadingDialog))
        .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(
            this::alert,
            e -> toast(e.getMessage())
        );
  }

  public void requestGankTodayList(View view) {
    RetrofitHelper.create(GankApi.class)
        .getGankTodayListByRxJava()
        .compose(Transformers.io2mainThread())
        .compose(Transformers.showLoading(getSupportFragmentManager(), loadingDialog))
        .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(
            this::alert,
            e -> toast(e.getMessage())
        );
  }

  public void requestLogin(View view) {
    RetrofitHelper.create(RxJavaApi.class)
        .login()
        .compose(Transformers.io2mainThread())
        .compose(Transformers.showLoading(getSupportFragmentManager(), loadingDialog))
        .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(
            response -> toast("登录成功"),
            e -> toast(e.getMessage())
        );
  }

  public void download(View view) {
    final String pathname = Objects.requireNonNull(getExternalCacheDir()).getPath() + "/test.png";
    RetrofitHelper.create(RxDownloadApi.class)
        .download(Constants.DOWNLOAD_URL)
        .compose(Transformers.toFile(pathname))
        .compose(Transformers.io2mainThread())
        .compose(Transformers.showLoading(getSupportFragmentManager(), loadingDialog))
        .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
        .subscribe(
            file -> toast("已下载到" + file.getPath()),
            e -> toast(e.getMessage())
        );
  }

  private void alert(String msg) {
    new AlertDialog.Builder(this)
        .setTitle("Response data")
        .setMessage(msg)
        .create()
        .show();
  }

  private void toast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }
}
