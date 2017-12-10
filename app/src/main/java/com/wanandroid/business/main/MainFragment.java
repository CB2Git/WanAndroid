package com.wanandroid.business.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanandroid.R;
import com.wanandroid.business.adapter.WanAndroidArticleAdapter;
import com.wanandroid.business.base.BaseMVPFragment;
import com.wanandroid.business.fun.OnArticleItemClickListener;
import com.wanandroid.business.webview.WebActivity;
import com.wanandroid.model.entity.Article;

import java.util.List;

/**
 * 显示首页数据的Fragment
 */
public class MainFragment extends BaseMVPFragment<MainContract.View, MainPresenter> implements MainContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainFragment";

    //预加载的数量，当滚动到只剩下5个的时候开始加载下一页
    private static final int PRELOAD_SIZE = 5;

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FloatingActionButton mBackTopButton;

    //布局管理器
    private LinearLayoutManager mLayoutManager;

    //首页列表适配器
    private WanAndroidArticleAdapter mArticleAdapter;

    //当前正在加载第几页
    private int mCurrPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        initView(root);
        loadData();
        return root;
    }

    private void initView(View root) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);

        mArticleAdapter = new WanAndroidArticleAdapter(getContext());

        //设置RecycleView
        mRecyclerView = (RecyclerView) root.findViewById(R.id.main_article_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mArticleAdapter);
        mRecyclerView.addOnScrollListener(getOnBottomListener());

        mArticleAdapter.setOnArticleItemClickListener(getOnArticleItemClickListener());

        //设置SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN);
        //保证首次能显示刷新
        mSwipeRefreshLayout.measure(0, 0);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mBackTopButton = (FloatingActionButton) root.findViewById(R.id.main_back_top);
        mBackTopButton.setOnClickListener(getOnBackTopListener());
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

    @Override
    public void onRefresh() {
        getBindPresenter().cancelCurrentLoadArticles();
        getBindPresenter().refreshArticles();
    }

    public boolean onBackPressed() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            getBindPresenter().cancelCurrentLoadArticles();
            return true;
        }
        return false;
    }

    @Override
    public void showLoading() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override

    public void hideLoading() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void hideLoadMore() {
        mArticleAdapter.setRefreshing(false);
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        //TODO Error提示
        Log.i(TAG, "showErrorMsg: " + errorMsg);
    }

    @Override
    public void displayArticles(List<Article> articles, boolean clearOld) {
        if (clearOld) {
            mArticleAdapter.setArticles(articles);
            mCurrPage = 0;
        } else {
            mArticleAdapter.addArticles(articles);
        }
    }

    /**
     * 返回顶部按钮点击监听
     */
    @NonNull
    private View.OnClickListener getOnBackTopListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecyclerView != null) {
                    mRecyclerView.smoothScrollToPosition(0);
                    //因为smoothScrollToPosition不会触发behavior，所以这里手动隐藏
                    ViewCompat.animate(mBackTopButton)
                            .scaleX(0.0f)
                            .scaleY(0.0f)
                            .setDuration(500)
                            .start();
                }
            }
        };
    }

    /**
     * RecycleView下滑底部刷新
     */
    @NonNull
    private RecyclerView.OnScrollListener getOnBottomListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = manager.findLastCompletelyVisibleItemPosition();
                //如果到达了预加载的零界点且不在刷新状态
                if (position == manager.getItemCount() - PRELOAD_SIZE
                        && !mArticleAdapter.isRefreshing()
                        && !mSwipeRefreshLayout.isRefreshing()) {
                    mArticleAdapter.setRefreshing(true);
                    mCurrPage += 1;
                    getBindPresenter().getArticles(mCurrPage);
                }
            }
        };
    }

    /**
     * 文章被点击的相关监听
     */
    public OnArticleItemClickListener getOnArticleItemClickListener() {
        return new OnArticleItemClickListener() {
            @Override
            public void OnArticleItemClick(Article article) {
                Intent intent = WebActivity.newInstance(getContext(), article.getLink());
                startActivity(intent);
            }
        };
    }
}
