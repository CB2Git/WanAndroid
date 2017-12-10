package com.wanandroid.model;

import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * 由于{@link com.wanandroid.model.api.WanAndroidApiCompat#searchArticle(String)}接口被弃用，所以
 * 此数据结构也被弃用，请参看新接口{@link com.wanandroid.model.api.WanAndroidApi#searchArticle(int, String)}
 * <br/>
 * 搜索结果数据
 */
@Deprecated
public class SearchData extends BaseResponseData {

    private List<Article> mArticles;

    public List<Article> getArticles() {
        return mArticles;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }
}
