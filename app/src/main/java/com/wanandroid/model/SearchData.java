package com.wanandroid.model;

import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * 搜索结果数据
 */
public class SearchData extends BaseResponseData {

    private List<Article> mArticles;

    public List<Article> getArticles() {
        return mArticles;
    }

    public void setArticles(List<Article> articles) {
        mArticles = articles;
    }
}
