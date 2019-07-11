package com.dylanc.retrofit.helper.sample;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * @author Dylan Cai
 * @since 2019/4/17
 */
public interface TestService {

  @GET("guonei")
  Observable<String> getBaiduNews();

  @Headers({"Domain-Name: gank"})
  @GET("/api/today")
  Observable<String> getGankData();

}
