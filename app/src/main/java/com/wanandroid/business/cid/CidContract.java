package com.wanandroid.business.cid;

import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * Created by ${jay} on ${2016/8/17
 */
public interface CidContract {

    interface View {

        void hideLoading();

        void hideLoadMore();

        void showNoMore();

        void showErrorMsg(String ErrorMsg);

        /**
         * 展示文章列表
         *
         * @param articles 获取到的文章数据
         * @param clearOld 是否清除旧数据
         */
        void displayArticles(List<Article> articles, boolean clearOld);
    }

    interface Presenter {

        void refreshCidDetail(int cidID);

        void loadNextCidPage();

        void cancelRefresh();
    }
}
