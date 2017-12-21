package com.wanandroid.business.collects;

import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * Created by ${jay} on ${2016/8/17
 */
public interface CollectContract {

    interface View {

        /**
         * 显示顶部刷新
         */
        void showPullTopRefresh();

        /**
         * 隐藏顶部刷新
         */
        void hidePullTopRefresh();


        /**
         * 隐藏底部刷新
         */
        void hideBottomRefresh();

        /**
         * 显示"没有更多"
         */
        void showNoMore();

        /**
         * 隐藏"没有更多"
         */
        void hideNoMore();

        /**
         * 显示加载更多失败
         */
        void showLoadMoreError();

        /**
         * 展示文章列表
         *
         * @param articles 获取到的文章数据
         * @param clearOld 是否清除旧数据
         */
        void displayArticles(List<Article> articles, boolean clearOld);
    }

    interface Presenter {

        /**
         * 刷新收藏列表
         */
        void refreshData();

        /**
         * 加载下一页数据
         */
        void loadNextPage();

        void cancelCurrentLoad();
    }
}
