package com.dylanc.retrofit.helper.transformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Dylan Cai
 * @since 2019/7/11
 */
public class ThreadTransformer {

  public static <T> ObservableTransformer<T, T> main() {
    return new ObservableTransformer<T, T>() {
      @Override
      public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
      }
    };
  }
}
