package com.wanandroid.model.api;

import com.wanandroid.model.ArticleData;
import com.wanandroid.model.AuthData;
import com.wanandroid.model.BaseResponseData;
import com.wanandroid.model.CollectedArticleData;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * WanAndroid的可用API
 */
public interface WanAndroidApi {

    //获取首页数据
    @GET("article/list/{page}/json")
    Observable<ArticleData> getArticleData(@Path("page") int page);

    //注册
    @POST("user/register")
    @FormUrlEncoded
    Observable<AuthData> register(@Field("username") String userName, @Field("password") String pwd, @Field("repassword") String rePwd);

    //登录
    @POST("user/login")
    @FormUrlEncoded
    Observable<AuthData> login(@Field("username") String userName, @Field("password") String pwd);

    //收藏列表
    @GET("lg/collect/list/{page}/json")
    Observable<CollectedArticleData> getCollectData(@Path("page") int page);

    //取消收藏文章
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResponseData> unCollectArticle(@Path("id") int id);

    //收藏文章
    @POST("lg/collect/{id}/json")
    Observable<BaseResponseData> collectArticle(@Path("id") int id);

    //获取分类数据
    @GET("article/list/{page}/json")
    Observable<ArticleData> getCidData(@Path("page") int page, @Query("cid") int cid);

    //搜索接口
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    Observable<ArticleData> searchArticle(@Path("page") int page, @Field("k") String key);
}
