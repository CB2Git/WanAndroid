package com.wanandroid.model.api.utils;

import com.wanandroid.BuildConfig;
import com.wanandroid.WanApplication;
import com.wanandroid.utils.NetUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OKHttp的主要配置都在这里
 */
public class OkHttpConfig {

    //设缓存有效期为1天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24;

    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    //用于控制是否输出日志
    public static final HttpLoggingInterceptor sLoggingInterceptor = new HttpLoggingInterceptor();

    //用户控制Http缓存
    public static final Interceptor sCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if ("GET".equals(request.method())) {
                return doCache(chain);
            } else {
                return chain.proceed(request);
            }
        }

        private Response doCache(Chain chain) throws IOException {
            Request request = chain.request();
            //如果没网，则只使用缓存
            if (!NetUtil.isNetworkAvailable(WanApplication.getAppContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }

            Response originalResponse = chain.proceed(request);

            if (NetUtil.isNetworkAvailable(WanApplication.getAppContext())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                //没网则缓存
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, " + CACHE_CONTROL_CACHE)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    static {
        sLoggingInterceptor.setLevel(BuildConfig.LOG_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

}
