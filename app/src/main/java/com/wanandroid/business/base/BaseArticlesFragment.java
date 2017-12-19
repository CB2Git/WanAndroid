package com.wanandroid.business.base;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanandroid.R;
import com.wanandroid.business.adapter.WanAndroidArticleAdapter;
import com.wanandroid.business.callback.OnArticleFragmentRefreshListener;
import com.wanandroid.business.callback.OnArticleItemClickListener;
import com.wanandroid.business.articledetail.ArticleDetailActivity;
import com.wanandroid.model.entity.Article;

/**
 * 由于多个界面(首页列表，搜索列表，分类列表，收藏列表)均需要显示文章列表，所以抽取为基类
 */
public abstract class BaseArticlesFragment<V, P extends BasePresenterImpl<V>> extends BaseMVPFragment<V, P> implements SwipeRefreshLayout.OnRefreshListener {

    //预加载的数量，当滚动到只剩下3个的时候开始加载下一页
    private static final int PRELOAD_SIZE = 3;

    //整体布局
    private View mRootView;

    //文章列表
    private RecyclerView mRecyclerView;

    //下拉刷新
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //是否允许上拉刷新
    private boolean mEnablePullBottomRefresh = true;

    //快速回到顶部
    private FloatingActionButton mBackTopButton;

    //布局管理器
    private LinearLayoutManager mLayoutManager;

    //首页列表适配器
    private WanAndroidArticleAdapter mArticleAdapter;

    //给子类的刷新回调
    private OnArticleFragmentRefreshListener mArticleFragmentRefreshListener;

    //给子类的文章点击回调
    private OnArticleItemClickListener mOnArticleItemClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = bindFragmentView(inflater, container, savedInstanceState);
        if (root.findViewById(R.id.article_layout) == null) {
            throw new IllegalStateException("you layout must include the fragment_article.xml");
        }
        initView(root);
        return root;
    }

    public abstract View bindFragmentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


    private void initView(View root) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);

        mArticleAdapter = new WanAndroidArticleAdapter(getContext());

        mRootView = root.findViewById(R.id.article_layout);

        //设置RecycleView
        mRecyclerView = (RecyclerView) root.findViewById(R.id.main_article_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mArticleAdapter);
        mRecyclerView.addOnScrollListener(getOnBottomListener());

        mArticleAdapter.setOnArticleItemClickListener(getOnArticleItemClickListener());

        //设置SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.article_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.YELLOW, Color.RED, Color.GREEN);
        //保证首次能显示刷新
        mSwipeRefreshLayout.measure(0, 0);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mBackTopButton = (FloatingActionButton) root.findViewById(R.id.article_back_top);
        mBackTopButton.setOnClickListener(getOnBackTopListener());
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
                    if (mEnablePullBottomRefresh) {
                        mArticleAdapter.setRefreshing(true);
                        if (mArticleFragmentRefreshListener != null) {
                            mArticleFragmentRefreshListener.onPullBottomRefresh();
                        }
                    }
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
                //如果子类没有设置点击响应，那么使用默认的操作
                if (mOnArticleItemClickListener == null) {
                    Intent intent = ArticleDetailActivity.newInstance(getContext(), article);
                    startActivity(intent);
                } else {
                    mOnArticleItemClickListener.OnArticleItemClick(article);
                }
            }
        };
    }

    @Override
    public void onRefresh() {
        if (mArticleFragmentRefreshListener != null) {
            mArticleFragmentRefreshListener.onPullTopRefresh();
        }
    }

    /*********************************/
    //          给子类的操作接口
    /*********************************/

    /**
     * 是否允许下拉刷新，默认为true
     */
    public void enablePullRefresh(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

    /**
     * 是否允许上拉加载更多，默认为true
     */
    public void enablePullBottomRefresh(boolean enable) {
        mEnablePullBottomRefresh = enable;
    }

    /**
     * 获取下拉控件
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    /**
     * 获取视图根布局
     *
     * @return
     */
    public View getRootView() {
        return mRootView;
    }

    /**
     * 获取适配器
     */
    public WanAndroidArticleAdapter getArticleAdapter() {
        return mArticleAdapter;
    }

    /**
     * 获取列表
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 设置文章列表item被点击的监听，如果不设置，默认使用WebActivity打开
     *
     * @param listener
     */
    public void setOnArticleItemClickListener(OnArticleItemClickListener listener) {
        mOnArticleItemClickListener = listener;
    }

    /**
     * 设置上拉下拉刷新监听
     *
     * @param listener
     */
    public void setOnArticleFragmentRefreshListener(OnArticleFragmentRefreshListener listener) {
        mArticleFragmentRefreshListener = listener;
    }
}
