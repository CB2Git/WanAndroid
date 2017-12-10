package com.wanandroid.business.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanandroid.R;
import com.wanandroid.business.adapter.WanAndroidArticleAdapter;
import com.wanandroid.business.base.BaseMVPFragment;
import com.wanandroid.business.fun.OnArticleItemClickListener;
import com.wanandroid.business.fun.OnSearchKeyClickListener;
import com.wanandroid.business.webview.WebActivity;
import com.wanandroid.model.entity.Article;
import com.wang.avi.AVLoadingIndicatorView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * 显示搜索结果的Fragment
 * TODO：搜索历史以后来一发
 * <p>
 * TODO:给搜索加一个清空(返回键~~)可好
 */
public class SearchFragment extends BaseMVPFragment<SearchContract.View, SearchPresenter> implements SearchContract.View {

    private static final String TAG = "SearchFragment";

    private static final int PRELOAD_SIZE = 5;

    //"大家都在搜"流式布局
    private TagFlowLayout mFlowLayout;

    //热搜
    private List<String> mHotKeys;

    //状态一:显示"大家都在搜"的布局
    private LinearLayout mHotKeysLayout;

    //状态二:显示加载动画，正在搜索
    private AVLoadingIndicatorView mAVIIndicator;

    //状态三:展示搜索结果
    private RecyclerView mRecyclerView;

    //状态四:搜索结果为空或者搜索出现了错误
    private TextView mSearchError;

    //当前正在搜索的关键词
    private String current_key;

    private WanAndroidArticleAdapter mArticleAdapter;

    private OnSearchKeyClickListener mOnSearchKeyClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchKeyClickListener) {
            mOnSearchKeyClickListener = (OnSearchKeyClickListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        initView(root);
        initData();
        return root;
    }

    private void initView(View root) {
        //初始化所有的状态布局
        mHotKeysLayout = (LinearLayout) root.findViewById(R.id.search_hot_key_layout);
        mAVIIndicator = (AVLoadingIndicatorView) root.findViewById(R.id.search_avi_indicator);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.search_article_list);
        mSearchError = (TextView) root.findViewById(R.id.search_error);

        mFlowLayout = (TagFlowLayout) root.findViewById(R.id.search_hot_search);
        mFlowLayout.setOnTagClickListener(getOnTagClickListener());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false));
        mRecyclerView.addOnScrollListener(getOnBottomListener());

        mArticleAdapter = new WanAndroidArticleAdapter(getContext());
        mArticleAdapter.setOnArticleItemClickListener(getOnArticleItemClickListener());

        mRecyclerView.setAdapter(mArticleAdapter);

        toggleViewStatue(-1);
    }

    private void initData() {
        getBindPresenter().getHotKeys();
    }


    @Override
    public void displayHotKeys(List<String> hotKeys) {
        Log.i(TAG, "displayHotKeys: " + hotKeys.size());
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        mHotKeys = hotKeys;
        mFlowLayout.setAdapter(new TagAdapter<String>(hotKeys) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView textView = (TextView) inflater.inflate(R.layout.hot_key_item, parent, false);
                textView.setText(s);
                return textView;
            }

        });
        //当搜索结果不可见的时候才显示热搜界面，以免当搜索结果快于热搜出现造成显示BUG
        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            toggleViewStatue(0);
        }
    }

    @Override
    public void displaySearchResult(List<Article> articles) {
        Log.i(TAG, "displaySearchResult: " + articles.size());
        if (articles.size() != 0) {
            mArticleAdapter.setArticles(articles);
            toggleViewStatue(2);
        } else {
            displaySearchError();
        }
    }

    @Override
    public void appendSearchResult(List<Article> articles) {
        mArticleAdapter.addArticles(articles);
        mArticleAdapter.setRefreshing(false);
    }

    @Override
    public void displayNoMore() {
        mArticleAdapter.setRefreshNoMore();
    }

    @Override
    public void displayLoadMoreError() {
        mArticleAdapter.setRefreshError();
    }

    @Override
    public void displaySearching() {
        toggleViewStatue(1);
    }

    @Override
    public void displaySearchError() {
        toggleViewStatue(3);
        String error = String.format(getString(R.string.search_error), current_key);
        mSearchError.setText(Html.fromHtml(error));
    }

    /**
     * 执行搜索操作
     *
     * @param key 多个关键词使用空格隔开
     */
    public void doSearch(String key) {
        Log.i(TAG, "doSearch: key=" + key);
        if (key == null || TextUtils.isEmpty(key.trim())) {
            Snackbar.make(getView(), R.string.search_key_error, Snackbar.LENGTH_LONG).show();
            return;
        }
        current_key = key.trim();
        getBindPresenter().doSearch(current_key);
    }

    @Override
    public SearchPresenter bindPresenter() {
        return new SearchPresenter();
    }

    @Override
    public SearchContract.View bindView() {
        return this;
    }


    /**
     * 将用户点击"大家都在搜"的事件传递给Activity统一进行处理
     *
     * @return
     */
    public TagFlowLayout.OnTagClickListener getOnTagClickListener() {
        return new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (mOnSearchKeyClickListener != null) {
                    mOnSearchKeyClickListener.OnSearchKeyClick(mHotKeys.get(position));
                }
                return false;
            }
        };
    }


    /**
     * 根据传递进来的状态进行视图显示/隐藏处理
     *
     * @param statue <br>
     *               -1- "全部不可见"<br>
     *               0 - "热搜" <br>
     *               1 - "正在搜索" <br>
     *               2 - "搜索结果"<br>
     *               3 - "搜索失败"
     */
    private void toggleViewStatue(int statue) {
        mHotKeysLayout.setVisibility(statue == 0 ? View.VISIBLE : View.INVISIBLE);
        mAVIIndicator.setVisibility(statue == 1 ? View.VISIBLE : View.INVISIBLE);
        if (statue == 1) {
            mAVIIndicator.show();
        } else {
            mAVIIndicator.hide();
        }
        mRecyclerView.setVisibility(statue == 2 ? View.VISIBLE : View.INVISIBLE);
        mSearchError.setVisibility(statue == 3 ? View.VISIBLE : View.INVISIBLE);
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
                        && !mArticleAdapter.isRefreshing()) {
                    mArticleAdapter.setRefreshing(true);
                    getBindPresenter().doSearchNextPage();
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
