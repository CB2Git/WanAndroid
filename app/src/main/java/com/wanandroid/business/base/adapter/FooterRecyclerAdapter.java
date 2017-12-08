package com.wanandroid.business.base.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * 拥有底部布局的RecycleAdapter
 */
public abstract class FooterRecyclerAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    public static final int VIEW_TYPE_NORMAL = 0;

    public static final int VIEW_TYPE_FOOTER = -1;

    //底部布局是否显示
    private boolean isFooterVisible = false;

    public FooterRecyclerAdapter() {
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) {
            return onCreateFooterViewHolder(parent);
        }
        return onCreateNormalViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_FOOTER) {
            onBindFooterViewHolder(holder);
            return;
        }
        onBindNormalViewHolder(holder, position);
    }

    /**
     * 创建普通的视图
     */
    protected abstract T onCreateNormalViewHolder(ViewGroup parent, int viewType);

    /**
     * 绑定普通的视图
     */
    protected abstract void onBindNormalViewHolder(T holder, int position);

    /**
     * 创建底部视图
     */
    protected abstract T onCreateFooterViewHolder(ViewGroup parent);

    /**
     * 绑定底部视图
     */
    protected abstract void onBindFooterViewHolder(T holder);

    /**
     * 获取正常布局的个数
     */
    public abstract int getNormalItemCount();

    @Override
    public int getItemCount() {
        return getNormalItemCount() + (isFooterVisible ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getNormalItemCount()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    /**
     * 对外接口，隐藏底部布局
     */
    public void hideFooter() {
        if (isFooterVisible) {
            deleteItem(getNormalItemCount());
            isFooterVisible = false;
        }
    }

    /**
     * 对外接口，显示底部布局
     */
    public void showFooter() {
        if (!isFooterVisible) {

            isFooterVisible = true;
            insertItem(getNormalItemCount());
        }
    }

    /**
     * 对外接口，显示底部布局
     */
    public void updateFooter() {
        if (!isFooterVisible) {
            showFooter();
        }
        updateItem(getItemCount() - 1);
    }

    /**
     * 对外接口，底部布局是否显示
     */
    public boolean isFooterVisible() {
        return isFooterVisible;
    }

    private void deleteItem(int position) {
        syncItem(position, 0);
    }


    private void insertItem(final int position) {
        syncItem(position, 1);
    }


    private void updateItem(final int position) {
        syncItem(position, 2);
    }

    private void syncItem(final int position, final int mode) {
        if (getItemCount() > position) {
            // Cannot call this method in a scroll callback.
            // Scroll callbacks might be run during a measure & layout pass where you cannot change the RecyclerView data.
            // Any method call that might change the structure of the RecyclerView or the adapter contents should be postponed to the next frame.
            Handler handler = new Handler();
            Runnable r = new Runnable() {
                public void run() {
                    switch (mode) {
                        //删除数据
                        case 0:
                            notifyItemRemoved(position);
                            break;
                        //插入数据
                        case 1:
                            notifyItemInserted(position);
                            break;
                        //更新数据
                        case 2:
                            notifyItemChanged(position);
                            break;
                    }
                }
            };
            handler.post(r);
        }
    }
}