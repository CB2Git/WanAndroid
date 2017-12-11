package com.wanandroid.business.cid;

import com.wanandroid.business.base.BasePresenterImpl;
import com.wanandroid.model.ArticleData;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.entity.Article;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class CidPresenter extends BasePresenterImpl<CidContract.View> implements CidContract.Presenter {

    private int mCurrPage = 0;

    private int mCurrCidId = -1;

    private Disposable mCurrRefreshDisposable;


    public void refreshCidDetail(final int cidID) {
        Disposable subscribe = WanAndroidRetrofitClient.getApiService()
                .getCidData(0, cidID)
                .map(new Function<ArticleData, List<Article>>() {
                    @Override
                    public List<Article> apply(@NonNull ArticleData articleData) throws Exception {
                        return articleData.getData().getDatas();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        getView().hideLoading();
                    }
                })
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> articles) throws Exception {
                        mCurrPage = 0;
                        mCurrCidId = cidID;
                        getView().displayArticles(articles, true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getView().showErrorMsg(throwable.getMessage());
                    }
                });
        mCurrRefreshDisposable = subscribe;
        addDisposable(subscribe);
    }

    @Override
    public void loadNextCidPage() {
        Disposable subscribe = WanAndroidRetrofitClient.getApiService()
                .getCidData(mCurrPage + 1, mCurrCidId)
                .map(new Function<ArticleData, List<Article>>() {
                    @Override
                    public List<Article> apply(@NonNull ArticleData articleData) throws Exception {
                        return articleData.getData().getDatas();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> articles) throws Exception {
                        if (articles.size() == 0) {
                            getView().showNoMore();
                        } else {
                            mCurrPage++;
                            getView().displayArticles(articles, false);
                            getView().hideLoading();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        addDisposable(subscribe);
    }

    @Override
    public void cancelRefresh() {
        if (mCurrRefreshDisposable != null && !mCurrRefreshDisposable.isDisposed()) {
            mCurrRefreshDisposable.dispose();
        }
    }
}
