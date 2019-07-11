package com.dylanc.retrofit.helper.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import com.dylanc.retrofit.helper.RetrofitHelper;
import com.dylanc.retrofit.helper.transformer.ThreadTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void requestData(View view) {
    RetrofitHelper.create(TestService.class)
        .getBaiduNews()
        .compose(ThreadTransformer.<String>main())
        .subscribe(new Observer<String>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(String s) {
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });
  }
}
