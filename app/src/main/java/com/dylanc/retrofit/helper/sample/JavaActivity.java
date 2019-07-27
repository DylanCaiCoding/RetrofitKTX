package com.dylanc.retrofit.helper.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.dylanc.retrofit.helper.DownloadService;
import com.dylanc.retrofit.helper.DownloadTask;
import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.helper.UploadUtils;
import com.dylanc.retrofit.helper.transformer.DownloadTransformer;
import com.dylanc.retrofit.helper.transformer.ThreadTransformer;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.io.File;

import static com.dylanc.retrofit.helper.sample.KotlinActivityKt.DOWNLOAD_URL;

public class JavaActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @SuppressLint("CheckResult")
  public void requestBaiduNews(View view) {
    RetrofitHelper.create(TestService.class)
        .getBaiduNews()
        .compose(ThreadTransformer.<String>main())
        .subscribe(new Consumer<String>() {
          @Override
          public void accept(String s) throws Exception {
            Toast.makeText(JavaActivity.this, s, Toast.LENGTH_SHORT).show();
          }
        }, new Consumer<Throwable>() {
          @Override
          public void accept(Throwable e) throws Exception {
            Toast.makeText(JavaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }
        });
  }

  public void requestGankData(View view) {
  }

  public void requestLogin(View view) {
    RetrofitHelper.create(TestService.class)
        .login()
        .compose(ThreadTransformer.<String>main())
        .subscribe(new Observer<String>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(String s) {
            Toast.makeText(JavaActivity.this, s, Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onError(Throwable e) {
            Toast.makeText(JavaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onComplete() {

          }
        });
  }

  public void download(View view) {
    final String pathname = Environment.getExternalStorageDirectory().getPath() + "/test.png";
    new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .flatMap(new Function<Boolean, ObservableSource<File>>() {
          @Override
          public ObservableSource<File> apply(Boolean granted) throws Exception {
            if (granted) {
              return DownloadTask.with(DOWNLOAD_URL).downloadTo(pathname);
            }
            throw new NullPointerException("");
          }
        })
        .subscribe(new Consumer<File>() {
          @Override
          public void accept(File file) throws Exception {
            Toast.makeText(JavaActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
          }
        });

  }
}
