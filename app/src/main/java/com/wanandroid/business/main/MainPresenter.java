package com.wanandroid.business.main;

import com.wanandroid.model.ArticleData;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.entity.Article;
import com.wanandroid.business.base.BasePresenterImpl;

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
public class MainPresenter extends BasePresenterImpl<MainContract.View> implements MainContract.Presenter {

    private Disposable mLastLoadArticles;

    public MainPresenter() {
    }

    @Override
    public void getArticles(int page) {
        mLastLoadArticles = WanAndroidRetrofitClient
                .getApiService()
                .getArticleData(page)
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
                        if (isViewAttached()) {
                            getView().hideLoadMore();
                        }
                    }
                })
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> articles) throws Exception {
                        if (isViewAttached()) {
                            getView().displayArticles(articles, false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (isViewAttached()) {
                            getView().showErrorMsg(throwable.getMessage());
                        }
                    }
                });
        addDisposable(mLastLoadArticles);
    }

    @Override
    public void refreshArticles() {
        mLastLoadArticles = WanAndroidRetrofitClient
                .getApiService()
                .getArticleData(0)
                .map(new Function<ArticleData, List<Article>>() {
                    @Override
                    public List<Article> apply(@NonNull ArticleData articleData) throws Exception {
                        return articleData.getData().getDatas();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (isViewAttached()) {
                            getView().showLoading();
                        }
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (isViewAttached()) {
                            getView().hideLoading();
                        }
                    }
                })
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> articles) throws Exception {
                        if (isViewAttached()) {
                            getView().displayArticles(articles, true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (isViewAttached()) {
                            getView().showErrorMsg(throwable.getMessage());
                        }
                    }
                });
        addDisposable(mLastLoadArticles);
    }

    @Override
    public void cancelCurrentLoadArticles() {
        if (mLastLoadArticles != null && !mLastLoadArticles.isDisposed()) {
            mLastLoadArticles.dispose();
        }
    }
}
