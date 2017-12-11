package com.wanandroid.business.main;

import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * 主界面契约类，主要定义P、V层的接口
 */
public interface MainContract {

    interface View {

        void hideLoading();

        void hideLoadMore();

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

        /**
         * 加载下一页数据
         */
        void loadNextPage();

        /**
         * 刷新所有数据，清除旧有数据
         */
        void refreshArticles();

        void cancelCurrentLoadArticles();
    }
}
