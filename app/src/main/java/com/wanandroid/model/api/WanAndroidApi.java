package com.wanandroid.model.api;

import com.wanandroid.model.ArticleData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ${jay} on ${2016/8/17
 */
public interface WanAndroidApi {

    //获取首页数据
    @GET("article/list/{page}/json")
    Observable<ArticleData> getArticleData(@Path("page") int page);

    //注册
    @POST("user/register")
    @FormUrlEncoded
    void register(@Field("username") String userName, @Field("password") String pwd, @Field("repassword") String rePwd);
}
