package com.wanandroid.business.articledetail;


import com.wanandroid.model.entity.WanAndroidUser;

public interface ArticleDetailContract {

    interface View {
        void collectSuccess();

        void collectFail();

        void unCollectSuccess();

        void unCollectFail();
    }

    interface Presenter {

        WanAndroidUser getUserInfo();

        void collectArticle(int id);

        void unCollectArticle(int id);
    }
}
