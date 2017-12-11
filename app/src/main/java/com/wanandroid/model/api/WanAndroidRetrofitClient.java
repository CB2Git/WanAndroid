package com.wanandroid.model.api;

import com.wanandroid.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * WanAndroid Api RetrofitClient管理类
 */
public class WanAndroidRetrofitClient {

    private static final String BASE_URL = "http://wanandroid.com/";

    private WanAndroidRetrofitClient() {

    }

    private static Retrofit mRetrofitClient = null;

    private static Object lockObj = new Object();

    private static Retrofit getRetroClient() {
        if (mRetrofitClient == null) {
            synchronized (lockObj) {
                if (mRetrofitClient == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    loggingInterceptor.setLevel(BuildConfig.LOG_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request request = chain.request();
                                    Request cookie = request.newBuilder()
                                            //.header("Cookie", "JSESSIONID=61BDF573D0FAD01B902B7295EF6235DE")
                                            .build();
                                    return chain.proceed(cookie);
                                }
                            })
                            .addNetworkInterceptor(loggingInterceptor)
                            .retryOnConnectionFailure(true)
                            .build();
                    mRetrofitClient = new Retrofit
                            .Builder()
                            .baseUrl(BASE_URL)
                            .client(okHttpClient)
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
