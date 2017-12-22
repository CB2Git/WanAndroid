package com.wanandroid.model.utils;

import com.wanandroid.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 由于一个应用只应该存在一个OkHttp客户端，所以通过这个管理
 */
public class OkHttpClientManger {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (OkHttpClientManger.class) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(BuildConfig.LOG_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .cookieJar(new WanAndroidCookieJar())
                            .addNetworkInterceptor(loggingInterceptor)
                            .retryOnConnectionFailure(true)
                            .build();
                }
            }
        }
        return okHttpClient;
    }
}
