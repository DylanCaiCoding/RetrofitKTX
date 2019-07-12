package com.dylanc.retrofit.helper;

import android.content.Context;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

/**
 * @author Dylan Cai
 * @since 2019/7/12
 */
public class PersistentCookie extends PersistentCookieJar {
  public PersistentCookie(Context context) {
    super(new SetCookieCache(),new SharedPrefsCookiePersistor(context));
  }

  public PersistentCookie(CookieCache cache, CookiePersistor persistor) {
    super(cache, persistor);
  }
}
