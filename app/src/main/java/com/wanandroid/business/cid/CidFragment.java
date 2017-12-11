package com.wanandroid.business.cid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanandroid.R;
import com.wanandroid.business.base.BaseArticlesFragment;
import com.wanandroid.business.fun.OnArticleFragmentRefreshListener;
import com.wanandroid.business.fun.OnClassifyClickListener;
import com.wanandroid.model.entity.Article;
import com.wanandroid.model.entity.Cid;

import java.util.List;

/**
 * 显示"知识体系分类"的Fragment
 */
public class CidFragment extends BaseArticlesFragment<CidContract.View, CidPresenter> implements CidContract.View, OnClassifyClickListener {

    private static final String TAG = "CidFragment";

    private Cid mCurrCid;

    @Override
    public View bindFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cid, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        enablePullRefresh(true);
        enablePullBottomRefresh(true);
        setOnArticleFragmentRefreshListener(getOnArticleFragmentRefreshListener());
    }

    @Override
    public CidPresenter bindPresenter() {
        return new CidPresenter();
    }

    @Override
    public CidContract.View bindView() {
        return this;
    }

    @Override
    public void onClassifyClickListener(Cid cid) {
        mCurrCid = cid;
        getBindPresenter().refreshCidDetail(mCurrCid.getCidId());
    }

    @Override
    public void hideLoading() {
        SwipeRefreshLayout swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void hideLoadMore() {
        getArticleAdapter().setRefreshing(false);
    }

    @Override
    public void showNoMore() {
        getArticleAdapter().setRefreshNoMore();
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        //TODO Error提示
        Log.i(TAG, "showErrorMsg: " + errorMsg);
    }

    @Override
    public void displayArticles(List<Article> articles, boolean clearOld) {
        if (clearOld) {
            getArticleAdapter().setArticles(articles);
            getRecyclerView().scrollToPosition(0);
        } else {
            getArticleAdapter().addArticles(articles);
        }
    }

    public OnArticleFragmentRefreshListener getOnArticleFragmentRefreshListener() {
        return new OnArticleFragmentRefreshListener() {
            @Override
            public void onPullTopRefresh() {
                getBindPresenter().cancelRefresh();
                getBindPresenter().refreshCidDetail(mCurrCid.getCidId());
            }

            @Override
            public void onPullBottomRefresh() {
                getArticleAdapter().setRefreshing(true);
                getBindPresenter().loadNextCidPage();
            }
        };
    }
}
