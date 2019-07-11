package com.dylanc.retrofit.helper;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

public class RetrofitManager {

  public static void setGlobalDomain(String globalDomain) {
    RetrofitUrlManager.getInstance().setGlobalDomain(globalDomain);
  }

  public static void putDomain(String domainName, String domainUrl) {
    RetrofitUrlManager.getInstance().putDomain(domainName, domainUrl);
  }

  public static void addRequestListener(String url, ProgressListener listener){
    ProgressManager.getInstance().addRequestListener(url,listener);
  }
}
