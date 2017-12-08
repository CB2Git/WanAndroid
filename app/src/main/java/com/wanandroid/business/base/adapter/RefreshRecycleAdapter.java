package com.wanandroid.business.base.adapter;

import android.support.v7.widget.RecyclerView;

import static com.wanandroid.business.base.adapter.RefreshRecycleAdapter.RefreshState.COMPLETED;
import static com.wanandroid.business.base.adapter.RefreshRecycleAdapter.RefreshState.LOADING;
import static com.wanandroid.business.base.adapter.RefreshRecycleAdapter.RefreshState.LOADING_ERROR;
import static com.wanandroid.business.base.adapter.RefreshRecycleAdapter.RefreshState.NO_MORE;

/**
 * 在{@link FooterRecyclerAdapter}基础上面延伸出来上拉刷新的RecycleAdapter
 */
public abstract class RefreshRecycleAdapter<T extends RecyclerView.ViewHolder> extends FooterRecyclerAdapter<T> {

    //"上拉加载"的视图状态
    private RefreshState mCurrRefreshState = COMPLETED;

    //"上拉加载"视图的几种状态
    public enum RefreshState {
        LOADING, LOADING_ERROR, COMPLETED, NO_MORE
    }

    @Override
    protected void onBindFooterViewHolder(T holder) {
        onBindRefreshFooterViewHolder(holder, mCurrRefreshState);
    }

    protected abstract void onBindRefreshFooterViewHolder(T holder, RefreshState state);

    public boolean isRefreshing() {
        return isFooterVisible() && mCurrRefreshState == LOADING;
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            mCurrRefreshState = LOADING;
            updateFooter();
        } else {
            mCurrRefreshState = COMPLETED;
            hideFooter();
        }
    }

    public void setRefreshNoMore() {
        mCurrRefreshState = NO_MORE;
        updateFooter();
    }

    public void setRefreshError() {
        mCurrRefreshState = LOADING_ERROR;
        updateFooter();
    }

}
