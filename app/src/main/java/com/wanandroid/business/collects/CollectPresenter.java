package com.wanandroid.business.collects;

import android.util.Log;

import com.wanandroid.business.base.BasePresenterImpl;
import com.wanandroid.model.CollectedArticleData;
import com.wanandroid.model.api.WanAndroidRetrofitClient;
import com.wanandroid.model.entity.Article;
import com.wanandroid.model.entity.CollectedArticle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ${jay} on ${2016/8/17
 */
public class CollectPresenter extends BasePresenterImpl<CollectContract.View> implements CollectContract.Presenter {

    private static final String TAG = "CollectPresenter";

    private int mCurrentPage = 0;

    @Override
    public void refreshData() {
        Disposable disposable = WanAndroidRetrofitClient
                .getApiService()
                .getCollectData(0)
                .map(new Function<CollectedArticleData, List<CollectedArticle>>() {
                    @Override
                    public List<CollectedArticle> apply(CollectedArticleData collectedArticleData) throws Exception {
                        return collectedArticleData.getData().getDatas();
                    }
                })
                .map(new Function<List<CollectedArticle>, List<Article>>() {
                    @Override
                    public List<Article> apply(List<CollectedArticle> collectedArticles) throws Exception {
                        List<Article> articles = new ArrayList<>();
                        articles.addAll(collectedArticles);
                        return articles;
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        getView().showPullTopRefresh();
                        getView().hideNoMore();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> collectedArticles) throws Exception {
                        mCurrentPage = 0;
                        getView().displayArticles(collectedArticles, true);
                        getView().hidePullTopRefresh();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept Throwable: " + throwable);
                        getView().hidePullTopRefresh();
                    }
                });
        addDisposable(disposable);
    }

    @Override
    public void loadNextPage() {
        Disposable disposable = WanAndroidRetrofitClient
                .getApiService()
                .getCollectData(mCurrentPage + 1)
                .map(new Function<CollectedArticleData, List<CollectedArticle>>() {
                    @Override
                    public List<CollectedArticle> apply(CollectedArticleData collectedArticleData) throws Exception {
                        return collectedArticleData.getData().getDatas();
                    }
                })
                .map(new Function<List<CollectedArticle>, List<Article>>() {
                    @Override
                    public List<Article> apply(List<CollectedArticle> collectedArticles) throws Exception {
                        List<Article> articles = new ArrayList<>();
                        articles.addAll(collectedArticles);
                        return articles;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(List<Article> collectedArticles) throws Exception {

                        if (collectedArticles == null || collectedArticles.size() == 0) {
                            getView().showNoMore();
                        } else {
                            ++mCurrentPage;
                            getView().displayArticles(collectedArticles, false);
                            getView().hideBottomRefresh();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "accept Throwable: " + throwable);
                        getView().showLoadMoreError();
                    }
                });
        addDisposable(disposable);
    }

    @Override
    public void cancelCurrentLoad() {

    }
}
