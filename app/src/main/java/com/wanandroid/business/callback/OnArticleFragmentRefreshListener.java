package com.wanandroid.business.callback;

/**
 * {@link com.wanandroid.business.base.BaseArticlesFragment}中的刷新回调
 */
public interface OnArticleFragmentRefreshListener {

    /**
     * 下拉刷新
     */
    void onPullTopRefresh();

    /**
     * 上拉刷新
     */
    void onPullBottomRefresh();
}
