package com.wanandroid.business.collects;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanandroid.R;
import com.wanandroid.business.base.BaseArticlesFragment;
import com.wanandroid.business.callback.OnArticleFragmentRefreshListener;
import com.wanandroid.business.callback.OnSetToolbarTitleCallBack;
import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * 显示收藏列表的Fragment
 */
public class CollectFragment extends BaseArticlesFragment<CollectContract.View, CollectPresenter> implements CollectContract.View {

    private OnSetToolbarTitleCallBack mOnSetToolbarTitleCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSetToolbarTitleCallBack) {
            mOnSetToolbarTitleCallBack = (OnSetToolbarTitleCallBack) context;
        }
    }

    @Override
    public CollectPresenter bindPresenter() {
        return new CollectPresenter();
    }

    @Override
    public CollectContract.View bindView() {
        return this;
    }

    @Override
    public View bindFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collect_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initDat();
    }

    private void initView() {
        if (mOnSetToolbarTitleCallBack != null) {
            mOnSetToolbarTitleCallBack.setToolbarTitle(getString(R.string.collect));
        }
        setOnArticleFragmentRefreshListener(getOnRefreshListener());
    }

    private void initDat() {
        getBindPresenter().refreshData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOnSetToolbarTitleCallBack != null) {
            mOnSetToolbarTitleCallBack.setToolbarTitle(getString(R.string.app_name));
        }
    }

    @Override
    public void showPullTopRefresh() {
        getSwipeRefreshLayout().setRefreshing(true);
    }

    @Override
    public void hidePullTopRefresh() {
        getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void hideBottomRefresh() {
        getArticleAdapter().setRefreshing(false);
    }

    @Override
    public void showNoMore() {
        getArticleAdapter().setRefreshNoMore();
    }

    @Override
    public void hideNoMore() {
        getArticleAdapter().setRefreshing(false);
    }

    @Override
    public void showLoadMoreError() {
        getArticleAdapter().setRefreshError();
    }

    @Override
    public void displayArticles(List<Article> articles, boolean clearOld) {
        if (clearOld) {
            getArticleAdapter().setArticles(articles);
        } else {
            getArticleAdapter().addArticles(articles);
        }
    }

    /***
     * 上拉，下拉监听
     */
    public OnArticleFragmentRefreshListener getOnRefreshListener() {
        return new OnArticleFragmentRefreshListener() {
            @Override
            public void onPullTopRefresh() {
                getBindPresenter().refreshData();
            }

            @Override
            public void onPullBottomRefresh() {
                getBindPresenter().loadNextPage();
            }
        };
    }
}
