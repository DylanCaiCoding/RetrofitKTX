package com.dylanc.retrofit.sample.java.network;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

/**
 * @author Dylan Cai
 */
public class AutoDisposable {

  public static <T> AutoDisposeConverter<T> from(LifecycleOwner lifecycleOwner) {
    return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner));
  }

  public static <T> AutoDisposeConverter<T> from(LifecycleOwner lifecycleOwner, Lifecycle.Event untilEvent) {
    return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner, untilEvent));
  }
}
