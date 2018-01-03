package com.wanandroid.model.api;

import com.wanandroid.model.utils.OkHttpClientManger;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * WanAndroid Api RetrofitClient管理类
 * @author Jay
 */
public class WanAndroidRetrofitClient {

    private static final String BASE_URL = "http://wanandroid.com/";

    private WanAndroidRetrofitClient() {

    }

    private static Retrofit mRetrofitClient = null;

    public static Retrofit getRetroClient() {
        if (mRetrofitClient == null) {
            synchronized (WanAndroidRetrofitClient.class) {
                if (mRetrofitClient == null) {
                    mRetrofitClient = new Retrofit
                            .Builder()
                            .baseUrl(BASE_URL)
                            .client(OkHttpClientManger.getOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return mRetrofitClient;
    }

    /**
     * 获取WanAndroid API
     */
    public static WanAndroidApi getApiService() {
        return getRetroClient().create(WanAndroidApi.class);
    }
}
