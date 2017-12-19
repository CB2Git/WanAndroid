package com.wanandroid.business.articledetail;

import android.util.Log;

import com.wanandroid.business.base.BasePresenterImpl;
import com.wanandroid.model.BaseResponseData;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.db.UserManger;
import com.wanandroid.model.entity.WanAndroidUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${jay} on ${2016/8/17
 */

public class ArticleDetailPresenter extends BasePresenterImpl<ArticleDetailContract.View> implements ArticleDetailContract.Presenter {

    private static final String TAG = "ArticleDetailPresenter";

    @Override
    public WanAndroidUser getUserInfo() {
        return UserManger.getUserInfo();
    }

    @Override
    public void collectArticle(int id) {
        Disposable subscribe = WanAndroidRetrofitClient
                .getApiService()
                .collectArticle(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BaseResponseData>() {
                    @Override
                    public void accept(BaseResponseData baseResponseData) throws Exception {
                        if (baseResponseData.errorCode == 0) {
                            getView().collectSuccess();
                        } else {
                            getView().collectFail();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + throwable.getMessage());
                    }
                });
        addDisposable(subscribe);
    }

    @Override
    public void unCollectArticle(int id) {
        Disposable disposable = WanAndroidRetrofitClient
                .getApiService()
                .unCollectArticle(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BaseResponseData>() {
                    @Override
                    public void accept(BaseResponseData baseResponseData) throws Exception {
                        if (baseResponseData.errorCode == 0) {
                            getView().unCollectSuccess();
                        } else {
                            getView().unCollectFail();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + throwable.getMessage());
                    }
                });
        addDisposable(disposable);
    }
}
