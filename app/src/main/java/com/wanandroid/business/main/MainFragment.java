package com.wanandroid.business.main;

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
import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * 显示首页数据的Fragment
 */
public class MainFragment extends BaseArticlesFragment<MainContract.View, MainPresenter> implements MainContract.View {

    private static final String TAG = "MainFragment";

    @Override
    public View bindFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData();
    }

    private void initView() {
        enablePullBottomRefresh(true);
        enablePullRefresh(true);
        setOnArticleFragmentRefreshListener(getOnArticleFragmentRefreshListener());
    }

    @Override
    public MainPresenter bindPresenter() {
        return new MainPresenter();
    }

    @Override
    public MainContract.View bindView() {
        return this;
    }

    private void loadData() {
        //TODO 做一个预加载
        getBindPresenter().refreshArticles();
    }

    public boolean onBackPressed() {
        SwipeRefreshLayout swipeRefreshLayout = getSwipeRefreshLayout();
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
            getBindPresenter().cancelCurrentLoadArticles();
            return true;
        }
        return false;
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
                getBindPresenter().cancelCurrentLoadArticles();
                getBindPresenter().refreshArticles();
            }

            @Override
            public void onPullBottomRefresh() {
                getArticleAdapter().setRefreshing(true);
                getBindPresenter().loadNextPage();
            }
        };
    }
}
