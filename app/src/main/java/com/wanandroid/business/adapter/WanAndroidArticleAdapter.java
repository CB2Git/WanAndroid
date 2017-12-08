package com.wanandroid.business.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanandroid.R;
import com.wanandroid.business.base.adapter.RefreshRecycleAdapter;
import com.wanandroid.business.fun.OnArticleItemClickListener;
import com.wanandroid.model.entity.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * WanAndroid文章列表的适配器
 */
public class WanAndroidArticleAdapter extends RefreshRecycleAdapter<WanAndroidArticleAdapter.ArticleHolder> {

    private Context mContext;

    private List<Article> mArticles;

    private OnArticleItemClickListener mItemClickListener;

    public WanAndroidArticleAdapter(Context context) {
        super();
        mContext = context;
        mArticles = new ArrayList<>();
    }

    @Override
    protected ArticleHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.main_article_item_layout, parent, false);
        return new ArticleHolder(itemView);
    }

    @Override
    protected void onBindNormalViewHolder(ArticleHolder holder, int position) {
        Article article = mArticles.get(position);
        holder.articleTitle.setText(article.getTitle());
        String detail_format = mContext.getString(R.string.article_detail_format);
        holder.articleDetail.setText(String.format(detail_format, article.getNiceDate(), article.getAuthor()));
    }

    @Override
    protected ArticleHolder onCreateFooterViewHolder(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View itemView = layoutInflater.inflate(R.layout.main_article_load_more_layout, parent, false);
        return new ArticleHolder(itemView);
    }

    @Override
    protected void onBindRefreshFooterViewHolder(ArticleHolder holder, RefreshState state) {
        if (state == RefreshState.LOADING) {
            TextView tv = (TextView) holder.loadMoreView.findViewById(R.id.tv_re);
            tv.setText(R.string.loading);
        }
        if (state == RefreshState.NO_MORE) {
            TextView tv = (TextView) holder.loadMoreView.findViewById(R.id.tv_re);
            tv.setText("没有更多了");
        }
        if (state == RefreshState.LOADING_ERROR) {
            TextView tv = (TextView) holder.loadMoreView.findViewById(R.id.tv_re);
            tv.setText("加载失败了");
        }
    }

    @Override
    public int getNormalItemCount() {
        return mArticles == null ? 0 : mArticles.size();
    }

    /**
     * 新增文章数据
     */
    public void addArticles(List<Article> articles) {
        mArticles.addAll(articles);
        //note:这里必须使用getItemCount(),不能使用实际数据的长度，因为有一个尾部布局！！！
        notifyItemRangeInserted(getItemCount(), articles.size());
    }

    /**
     * 重置文章数据
     */
    public void setArticles(List<Article> articles) {
        mArticles.clear();
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    public void setOnArticleItemClickListener(OnArticleItemClickListener listener) {
        mItemClickListener = listener;
    }

    /**
     * 这里偷懒用一个Holder了，所以loadMoreView与上面三个互斥为NULL (*^__^*)
     */
    protected class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView articleTitle;

        public TextView articleDetail;

        private LinearLayout articleItem;

        public View loadMoreView;

        public ArticleHolder(View itemView) {
            super(itemView);
            articleTitle = (TextView) itemView.findViewById(R.id.tv_article_title);
            articleDetail = (TextView) itemView.findViewById(R.id.tv_article_detail);
            articleItem = (LinearLayout) itemView.findViewById(R.id.ll_article_item);
            loadMoreView = itemView.findViewById(R.id.ll_load_more);
            if (articleDetail != null) {
                articleItem.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener == null)
                return;

            //打开文章
            if (v.getId() == R.id.ll_article_item) {
                mItemClickListener.OnArticleItemClick(mArticles.get(getLayoutPosition()));
            }
        }
    }
}
