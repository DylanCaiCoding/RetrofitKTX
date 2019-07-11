package com.dylanc.retrofit.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.dylanc.retrofit.helper.interceptor.DebugInterceptor;
import com.dylanc.retrofit.helper.interceptor.HeaderInterceptor;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RetrofitHelper {

  public static Default getDefault() {
    return Default.getInstance();
  }

  public static <T> T create(final Class<T> service) {
    return getRetrofit().create(service);
  }

  private static Retrofit getRetrofit() {
    return RetrofitHolder.RETROFIT;
  }

  private static final class RetrofitHolder {
    private static final String BASE_URL = RetrofitHelper.getDefault().getBaseUrl();
    private static final Retrofit RETROFIT = createRetrofit();

    @NonNull
    private static Retrofit createRetrofit() {
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BASE_URL)
          .addConverterFactory(ScalarsConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .client(OkHttpHolder.OK_HTTP_CLIENT)
          .build();
      if (!getDefault().getDomains().isEmpty()){
        for (Map.Entry<String,String> entry:getDefault().getDomains().entrySet()){
          RetrofitManager.putDomain(entry.getKey(),entry.getValue());
        }
      }
      return retrofit;
    }
  }

  private static final class OkHttpHolder {
    private static final int TIME_OUT = 60;
    private static final OkHttpClient OK_HTTP_CLIENT = createOkHttpClient();

    @NonNull
    private static OkHttpClient createOkHttpClient() {
      OkHttpClient.Builder builder = new OkHttpClient.Builder()
          .retryOnConnectionFailure(false)
          .connectTimeout(TIME_OUT, TimeUnit.SECONDS);
      builder = RetrofitUrlManager.getInstance().with(builder);
      builder = ProgressManager.getInstance().with(builder);
      for (Interceptor interceptor : getDefault().getInterceptors()) {
        builder.addInterceptor(interceptor);
      }
      if (!getDefault().getHeaders().isEmpty()){
        builder.addInterceptor(new HeaderInterceptor(getDefault().getHeaders()));
      }
      if (getDefault().getCookieJar()!=null){
        builder.cookieJar(getDefault().getCookieJar());
      }
      return builder.build();
    }
  }

  public static class Default {

    private String mBaseUrl;
    private String mDebugUrl;
    private HashMap<String,String> mHeaders = new HashMap<>();
    private ArrayList<Interceptor> mInterceptors = new ArrayList<>();
    private ArrayList<Interceptor> mDebugInterceptors = new ArrayList<>();
    private HashMap<String,String> mDomains = new HashMap<>();
    private CookieJar mCookieJar;
    private boolean mDebug;

    private Default(){}

    static Default getInstance() {
      return DefaultHolder.INSTANCE;
    }

    public Default setBaseUrl(String baseUrl) {
      mBaseUrl = baseUrl;
      return this;
    }

    public Default setDebugUrl(String debugUrl) {
      mDebugUrl = debugUrl;
      return this;
    }

    public Default setDebugMode(boolean debug) {
      mDebug = debug;
      return this;
    }

    public Default putDomain(String domainName, String domainUrl) {
      mDomains.put(domainName,domainUrl);
      return this;
    }

    public Default addHeader(String name, String value){
      mHeaders.put(name,value);
      return this;
    }

    public Default addDebugInterceptor(Context context, String debugUrl, int debugRawId) {
      return addInterceptor(new DebugInterceptor(context, debugUrl, debugRawId));
    }

    public Default addLoggingInterceptor(HttpLoggingInterceptor.Logger logger) {
      return addLoggingInterceptor(HttpLoggingInterceptor.Level.BODY, logger);
    }

    public Default addLoggingInterceptor(HttpLoggingInterceptor.Level level, HttpLoggingInterceptor.Logger logger) {
      HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
      loggingInterceptor.level(level);
      mDebugInterceptors.add(loggingInterceptor);
      return this;
    }

    @SuppressWarnings("WeakerAccess")
    public Default addInterceptor(Interceptor interceptor) {
      if (interceptor instanceof DebugInterceptor) {
        mDebugInterceptors.add(interceptor);
      } else {
        mInterceptors.add(interceptor);
      }
      return this;
    }

    public Default addInterceptors(ArrayList<Interceptor> interceptors) {
      for (Interceptor interceptor : interceptors) {
        addInterceptor(interceptor);
      }
      return this;
    }

    public Default setCookieJar(CookieJar cookieJar) {
      mCookieJar = cookieJar;
      return this;
    }

    public void init() {
      if (mDebug) {
        if (!TextUtils.isEmpty(mDebugUrl)) {
          mBaseUrl = mDebugUrl;
        }
        mInterceptors.addAll(mDebugInterceptors);
      }
    }

    String getBaseUrl() {
      return mBaseUrl;
    }

    CookieJar getCookieJar() {
      return mCookieJar;
    }

    ArrayList<Interceptor> getInterceptors() {
      return mInterceptors;
    }

     HashMap<String, String> getHeaders() {
      return mHeaders;
    }

    HashMap<String, String> getDomains() {
      return mDomains;
    }

    static class DefaultHolder {
      static final Default INSTANCE = new Default();
    }
  }

}
