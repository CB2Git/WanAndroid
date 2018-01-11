package com.wanandroid.model.api.utils;

import com.wanandroid.WanApplication;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * 由于一个应用只应该存在一个OkHttp客户端，所以通过这个管理
 */
public class OkHttpClientManger {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkHttpClientManger.class) {
                // 指定缓存路径,缓存大小50Mb
                Cache cache = new Cache(new File(WanApplication.getAppContext().getExternalCacheDir(), "HttpCache"), 1024 * 1024 * 50);
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .cookieJar(new WanAndroidCookieJar())
                            .addNetworkInterceptor(OkHttpConfig.sCacheControlInterceptor)
                            .addInterceptor(OkHttpConfig.sCacheControlInterceptor)
                            .addNetworkInterceptor(OkHttpConfig.sLoggingInterceptor)
                            .retryOnConnectionFailure(true)
                            .build();
                }
            }
        }
        return okHttpClient;
    }
}
