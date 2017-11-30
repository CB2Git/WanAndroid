package com.wanandroid.model.api;

import okhttp3.OkHttpClient;
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

    private static Retrofit getRetroClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return null;
//                    }
//                })
//                .cookieJar(new CookieJar() {
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        return null;
//                    }
//                })
                .build();

        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 获取WanAndroid API
     */
    public static WanAndroidApi getApiService() {
        return getRetroClient().create(WanAndroidApi.class);
    }
}
