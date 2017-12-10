package com.wanandroid.business.search;

import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * Created by ${jay} on ${2016/8/17
 */

public interface SearchContract {

    interface View {
        /**
         * 显示"大家都在搜"
         */
        void displayHotKeys(List<String> hotKeys);

        /**
         * 显示搜索结果
         */
        void displaySearchResult(List<Article> articles);

        /**
         * 添加搜索结果
         */
        void appendSearchResult(List<Article> articles);

        /**
         * 显示没有更多数据
         */
        void displayNoMore();

        /**
         * 加载更多数据出错
         */
        void displayLoadMoreError();

        /**
         * 显示正在搜索的视图
         */
        void displaySearching();

        /**
         * 显示搜索失败的视图
         */
        void displaySearchError();
    }

    interface Presenter {
        /**
         * 获取大家都在搜
         */
        void getHotKeys();

        /**
         * 执行搜索操作
         */
        void doSearch(String key);

        /**
         * 获取下一页
         */
        void doSearchNextPage();
    }
}
